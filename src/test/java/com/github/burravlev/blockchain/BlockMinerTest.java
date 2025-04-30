package com.github.burravlev.blockchain;

import com.github.burravlev.util.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class BlockMinerTest {
    @Test
    public void testMining() {
        BlockMiner miner = new BlockMiner();
        Block block = miner.mine("hash", new Wallet(), 1, getTransactions());
        log.info(JsonSerializer.serialize(block));
    }

    private static List<Transaction> getTransactions() {
        return List.of(
            new Transaction()
                .setId("id")
                .setPrevBlockHash("hash")
                .setAmount("10")
                .setSender("sender")
                .setReceiver("receiver")
                .setTimestamp(ZonedDateTime.now().toEpochSecond())
                .setSender("sign")
        );
    }
}
