package com.github.burravlev.node;

import com.github.burravlev.blockchain.*;
import com.github.burravlev.network.Node;
import com.github.burravlev.network.NodeBuilder;
import com.github.burravlev.storage.Storage;
import com.github.burravlev.storage.StorageFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class NodeInstance {
    private final Node node;
    @Getter
    @Accessors(fluent = true)
    private final Wallet wallet;
    private final Blockchain blockchain;
    private final BalanceLoader balanceLoader;

    @SneakyThrows
    public static NodeInstance run(NodeConfig config) {
        Wallet wallet;
        if (config.getKey() == null) {
            wallet = new Wallet();
        } else {
            wallet = new Wallet(config.getKey());
        }
        Node node = new NodeBuilder()
            .privateKey(config.getKey())
            .addresses(config.getAddresses())
            .port(config.getPort())
            .build();
        node.start();
        Storage storage = StorageFactory.byFile(config.getStoragePath());
        Blockchain blockchain = new DefaultBlockchain(
            wallet, storage, new NewBlockPublisher(node)
        );
        new BlockUploader(node, blockchain);
        new NewBlockListener(blockchain, node);
        new GetBalanceListener(node, blockchain);
        ChainRestorer restorer = new ChainRestorer(node, blockchain.restorer());
        restorer.restore();
        if (blockchain.size() == 0) {
            log.info("mine genesis");
            blockchain.mineGenesis("Test data for test blockchain!");
            log.info("finish mining genesis");
        }
        new TransactionListener(node, blockchain);
        return new NodeInstance(
            node,
            wallet,
            blockchain,
            new BalanceLoader(node, blockchain)
        );
    }

    public void transfer(String address, String amount) {
        try {
            Transaction tx = blockchain.transfer(wallet, address, amount);
            node.sendToTopic(Topics.NEW_TX, tx);
        } catch (NotEnoughBalance e) {
            log.error("Not enough coins");
        }
    }

    public String balance() {
        return balanceLoader.getUserBalance(wallet.getAddress())
            .toPlainString();
    }
}
