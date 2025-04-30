package com.github.burravlev.blockchain;

import com.github.burravlev.storage.Storage;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
class SqlTxStorage implements TxStorage {
    private final Storage storage;

    SqlTxStorage(Storage storage) {
        this.storage = storage;
        storage.execute(BlockchainScripts.TX);
    }

    @Override
    public void save(Transaction tx) {
        storage.execute("""
                INSERT INTO tx(
                    id, rand_bytes, prev_block_hash, sender, receiver, amount, timestamp, signature
                ) VALUES(?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING""",
            tx.getId(),
            tx.getRandBytes(),
            tx.getPrevBlockHash(),
            tx.getSender(),
            tx.getReceiver(),
            tx.getAmount(),
            tx.getTimestamp(),
            tx.getSignature()
        );
    }

    @Override
    public List<Transaction> findNotAccepted() {
        return storage.executeQuery("SELECT * FROM tx WHERE curr_block_hash IS NULL", new TransactionMapper());
    }

    @Override
    public void markAccepted(Collection<String> ids, String blockHash) {
        String idsCondition = "(" + ids.stream()
            .map(id -> "'" + id + "'")
            .collect(Collectors.joining(",")) + ")";
        storage.execute("UPDATE tx SET curr_block_hash = ? WHERE id IN " + idsCondition, blockHash);
    }

    @Override
    public List<Transaction> getTransactions(String address) {
        return storage.executeQuery("SELECT * FROM tx WHERE sender = ? OR receiver = ?",
            new TransactionMapper(),
            address, address);
    }

    @Override
    public List<Transaction> getByHash(String hash) {
        return storage.executeQuery("SELECT * FROM tx WHERE curr_block_hash = ?",
            new TransactionMapper(), hash);
    }
}
