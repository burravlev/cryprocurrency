package com.github.burravlev.blockchain;

import com.github.burravlev.storage.RowMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.burravlev.blockchain.BlockchainTestData.getBlock;

public class TmpChainStorageTest extends AbstractChainTest {
    private static final RowMapper<List<Block>> BLOCK_MAPPER = rs -> {
        List<Block> result = new ArrayList<>();
        try {
            while (rs.next()) {
                var block = new Block()
                    .setHash(rs.getString("hash"));
                result.add(block);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private static final RowMapper<List<Transaction>> TX_MAPPER = rs -> {
        List<Transaction> result = new ArrayList<>();
        try {
            while (rs.next()) {
                var tx = new Transaction()
                    .setCurrBlockHash(rs.getString("curr_block_hash"));
                result.add(tx);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    @Test
    public void shouldStoreAndFlush() throws Exception {
        var storage = testStorage();
        TmpChainStorage tmpChainStorage = new SqlTmpChainStorage(storage);
        tmpChainStorage.init();
        int nodes = 5;
        String hash1 = "hash1";
        String hash2 = "hash2";
        for (int i = 0; i < nodes; i++) {
            tmpChainStorage.add(getBlock(hash1));
        }
        tmpChainStorage.add(getBlock(hash2));
        tmpChainStorage.flush(nodes - 2);

        var blocks = storage.executeQuery("SELECT * FROM blockchain", BLOCK_MAPPER);
        var blocksTmp = storage.executeQuery("SELECT * FROM blockchain_tmp", BLOCK_MAPPER);
        Assertions.assertEquals(1, blocks.size());
        Assertions.assertTrue(blocksTmp.isEmpty());

        var tx = storage.executeQuery("SELECT * FROM tx", TX_MAPPER);
        var txTmp = storage.executeQuery("SELECT * FROM tx_tmp", TX_MAPPER);
        Assertions.assertEquals(1, tx.size());
        Assertions.assertTrue(txTmp.isEmpty());

        storage.close();
    }
}
