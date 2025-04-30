package com.github.burravlev.blockchain;

import com.github.burravlev.storage.Storage;
import com.github.burravlev.util.Sha256Util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DefaultBlockchain implements Blockchain {
    private static final String REWARD = "19000000";
    private static final int TX_THRESHOLD = 10;

    private final AtomicInteger pendingTxCount = new AtomicInteger();

    private final Wallet wallet;
    private final TxStorage txStorage;
    private final ChainStorage chainStorage;
    private final TmpChainStorage tmpChainStorage;
    private final EventPublisher publisher;
    private final Miner miner;
    private final BlockMiner blockMiner;
    private final DifficultyCalculator difficultyCalculator;

    public DefaultBlockchain(Wallet wallet, Storage storage, EventPublisher publisher) {
        this.wallet = wallet;
        this.txStorage = new SqlTxStorage(storage);
        this.chainStorage = new SqlChainStorage(storage);
        this.tmpChainStorage = new SqlTmpChainStorage(storage);
        this.publisher = publisher;
        this.blockMiner = new BlockMiner();
        this.difficultyCalculator = new DifficultyCalculator() {
        };
        this.miner = getMiner();
    }

    @Override
    public void mineGenesis(String data) {
        Wallet genesisBlock = new Wallet();
        transferGenesis(genesisBlock, wallet.getAddress(), REWARD);
        var transactions = txStorage.findNotAccepted();
        Block block = blockMiner.mine(Sha256Util.hash(data), wallet, difficultyCalculator.calc(), transactions);
        chainStorage.save(block);
        txStorage.markAccepted(transactions.stream()
                .map(Transaction::getId)
                .collect(Collectors.toSet()),
            block.getHash()
        );
    }

    private Miner getMiner() {
        return new DefaultMiner(
            txStorage,
            chainStorage,
            difficultyCalculator,
            publisher,
            wallet,
            blockMiner
        );
    }

    @Override
    public Transaction transfer(Wallet from, String to, String amount) {
        String prevHash = chainStorage.getLastHash();
        BigDecimal balance = getBalance(from.getAddress());
        if (balance.compareTo(new BigDecimal(amount)) < 0) {
            throw new NotEnoughBalance();
        }
        var tx = new Transaction(prevHash, from, to, amount);
        addTx(tx);
        return tx;
    }

    private Transaction transferGenesis(Wallet from, String to, String amount) {
        String prevHash = chainStorage.getLastHash();
        var tx = new Transaction(prevHash, from, to, amount);
        addTx(tx);
        return tx;
    }

    @Override
    public void addTx(Transaction tx) {
        pendingTxCount.incrementAndGet();
        if (pendingTxCount.get() == TX_THRESHOLD) {
            pendingTxCount.set(0);
            miner.mine();
        }
        txStorage.save(tx);
    }

    @Override
    public BigDecimal getBalance(String address) {
        var transactions = txStorage.getTransactions(address);
        BigDecimal balance = BigDecimal.ZERO;
        for (var transaction : transactions) {
            BigDecimal amount = new BigDecimal(transaction.getAmount());
            if (Objects.equals(address, transaction.getSender())) {
                balance = balance.subtract(amount);
            } else {
                balance = balance.add(amount);
            }
        }
        return balance;
    }

    @Override
    public BlockchainRestorer restorer() {
        return new ChainStorageRestorer(tmpChainStorage);
    }

    @Override
    public List<Block> getBlocks() {
        List<Block> blocks = chainStorage.getBlocks();
        for (Block block : blocks) {
            block.setTransactions(txStorage.getByHash(block.getHash()));
        }
        return blocks;
    }

    @Override
    public int size() {
        return chainStorage.count();
    }
}
