package com.github.burravlev.node;

import com.github.burravlev.blockchain.Block;
import com.github.burravlev.blockchain.Transaction;

import java.time.ZonedDateTime;
import java.util.List;

public class BlockchainTestData {
    public static final long DEFAULT_TIME = ZonedDateTime.now().toEpochSecond();

    public static Block getBlock(String hash) {
        return new Block()
            .setNonce(0)
            .setDifficulty(0)
            .setPrevHash(hash)
            .setMiner("miner")
            .setHash(hash)
            .setTimestamp(DEFAULT_TIME)
            .setTransactions(getTransactions(hash))
            .setSignature("sign");
    }

    public static List<Transaction> getTransactions(String hash) {
        return List.of(
            new Transaction()
                .setId("id")
                .setRandBytes("rand")
                .setPrevBlockHash(hash)
                .setSignature("sign")
                .setAmount("0.1")
                .setSender("sender")
                .setReceiver("receiver")
                .setCurrBlockHash(hash)
                .setTimestamp(DEFAULT_TIME)
        );
    }
}
