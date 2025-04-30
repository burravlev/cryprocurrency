package com.github.burravlev;

import com.github.burravlev.blockchain.Block;
import com.github.burravlev.blockchain.Transaction;
import lombok.experimental.UtilityClass;

import java.time.ZonedDateTime;
import java.util.List;

@UtilityClass
public class TestData {
    public static final String HASH = "hash";
    public static final long TIMESTAMP = ZonedDateTime.now().toEpochSecond();

    public static List<Transaction> getTransactions() {
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

    public static Block getBlock() {
        return new Block()
            .setNonce(0)
            .setHash(HASH)
            .setTransactions(getTransactions())
            .setTimestamp(TIMESTAMP)
            .setSignature("SIGNATURE")
            .setMiner("miner")
            .setDifficulty(1);
    }
}
