package com.github.burravlev.blockchain;

import com.github.burravlev.storage.Storage;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
class SqlChainStorage implements ChainStorage {
    private final Storage storage;

    SqlChainStorage(Storage storage) {
        this.storage = storage;
        storage.execute(BlockchainScripts.CHAIN);
    }

    @Override
    public void save(Block block) {
        storage.execute("""
                INSERT INTO blockchain(nonce, difficulty, prev_hash, miner, hash, timestamp, signature)
                VALUES(?, ?, ?, ?, ?, ?, ?) ON CONFLICT DO NOTHING""",
            block.getNonce(),
            block.getDifficulty(),
            block.getPrevHash(),
            block.getMiner(),
            block.getHash(),
            block.getTimestamp(),
            block.getSignature()
        );
    }

    @Override
    public String getLastHash() {
        return storage.executeQuery("SELECT hash FROM blockchain ORDER BY timestamp DESC LIMIT 1",
            rs -> {
                try {
                    String hash = rs.getString("hash");
                    if (hash == null) return "";
                    return hash;
                } catch (Exception e) {
                    log.error("cannot get latest hash: ", e);
                    throw new RuntimeException(e);
                }
            });
    }

    @Override
    public int count() {
        return storage.executeQuery("SELECT count(*) FROM blockchain", rs -> {
            try {
                return rs.getInt(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<Block> getBlocks() {
        return storage.executeQuery("SELECT * FROM blockchain", rs -> {
            try {
                List<Block> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(new Block()
                        .setNonce(rs.getInt("nonce"))
                        .setDifficulty(rs.getInt("difficulty"))
                        .setPrevHash(rs.getString("prev_hash"))
                        .setMiner(rs.getString("miner"))
                        .setHash(rs.getString("hash"))
                        .setTimestamp(rs.getLong("timestamp"))
                        .setSignature(rs.getString("signature"))
                    );
                }
                return result;
            } catch (Exception e) {
                log.error("cannot get blocks", e);
                return Collections.emptyList();
            }
        });
    }
}
