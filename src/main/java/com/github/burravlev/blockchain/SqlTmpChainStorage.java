package com.github.burravlev.blockchain;

import com.github.burravlev.storage.Storage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SqlTmpChainStorage implements TmpChainStorage {
    private final Storage storage;

    SqlTmpChainStorage(Storage storage) {
        this.storage = storage;
        init();
    }

    @Override
    public void init() {
        storage.execute(BlockchainScripts.TMP_CHAIN);
        storage.execute(BlockchainScripts.CHAIN);
        storage.execute(BlockchainScripts.TMP_TX);
        storage.execute(BlockchainScripts.TX);
    }

    @Override
    public void add(Block block) {
        storage.execute("""
                INSERT INTO blockchain_tmp(hash, nonce, difficulty, prev_hash, miner, timestamp, signature)
                VALUES(?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO UPDATE SET count = count + 1""",
            block.getHash(), block.getNonce(), block.getDifficulty(),
            block.getPrevHash(), block.getMiner(), block.getTimestamp(),
            block.getSignature());
        for (var tx : block.getTransactions()) {
            add(tx);
        }
    }

    private void add(Transaction tx) {
        storage.execute("""
                INSERT INTO tx_tmp(id, rand_bytes, prev_block_hash, curr_block_hash, sender, receiver, amount, timestamp, signature)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO UPDATE SET count = count + 1""",
            tx.getId(), tx.getRandBytes(), tx.getPrevBlockHash(), tx.getCurrBlockHash(), tx.getSender(),
            tx.getReceiver(), tx.getAmount(), tx.getTimestamp(), tx.getSignature()
        );
    }

    @Override
    public void flush(int threshold) {
        storage.execute("""
            DELETE FROM blockchain_tmp WHERE count < ?""", threshold);
        storage.execute("""
            DELETE FROM tx_tmp WHERE count < ?""", threshold);
        storage.execute("""
            INSERT INTO blockchain(hash, nonce, difficulty, prev_hash, miner, timestamp, signature)
            SELECT hash, nonce, difficulty, prev_hash, miner, timestamp, signature FROM blockchain_tmp
            WHERE true ON CONFLICT DO NOTHING
            """);
        storage.execute("""
            INSERT INTO tx(id, rand_bytes, prev_block_hash, curr_block_hash, sender, receiver, amount, signature, timestamp)
            SELECT id, rand_bytes, prev_block_hash, curr_block_hash, sender, receiver, amount, signature, timestamp FROM tx_tmp
            WHERE true ON CONFLICT DO NOTHING
            """);
        storage.execute("""
            DELETE FROM blockchain_tmp""");
        storage.execute("""
            DELETE FROM tx_tmp""");
    }

    @Override
    public int size() {
        return storage.executeQuery("select count(*) from blockchain_tmp", rs -> {
            try {
                return rs.getInt(1);
            } catch (Exception e) {
                return 0;
            }
        });
    }
}
