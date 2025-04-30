package com.github.burravlev.blockchain;

abstract class BlockchainScripts {
    static final String CHAIN = """
        CREATE TABLE IF NOT EXISTS blockchain(
            hash VARCHAR(256) UNIQUE NOT NULL,
            nonce INTEGER NOT NULL,
            difficulty INTEGER NOT NULL,
            prev_hash VARCHAR(256),
            miner TEXT NOT NULL,
            timestamp BIGINTEGER NOT NULL,
            signature TEXT NOT NULL,
            UNIQUE(hash, nonce, difficulty, prev_hash, miner, timestamp, signature)
        )""";
    static final String TX = """
        CREATE TABLE IF NOT EXISTS tx(
            id VARCHAR(256),
            rand_bytes TEXT NOT NULL,
            prev_block_hash VARCHAR(256),
            sender TEXT,
            receiver TEXT,
            amount TEXT,
            timestamp BIGINTEGER,
            signature TEXT,
            curr_block_hash VARCHAR(256),
            UNIQUE(id, rand_bytes, prev_block_hash, curr_block_hash, sender, receiver, amount, timestamp, signature)
        )
        """;
    static final String TMP_CHAIN = """
        CREATE TABLE IF NOT EXISTS blockchain_tmp(
            hash VARCHAR(256) NOT NULL,
            nonce INTEGER NOT NULL,
            difficulty INTEGER NOT NULL,
            prev_hash VARCHAR(256),
            miner TEXT NOT NULL,
            timestamp BIGINTEGER NOT NULL,
            signature TEXT NOT NULL,
            count INTEGER NOT NULL DEFAULT 1,
            UNIQUE(hash, nonce, difficulty, prev_hash, miner, timestamp, signature)
        )""";
    static final String TMP_TX = """
        CREATE TABLE IF NOT EXISTS tx_tmp(
            id VARCHAR(256),
            rand_bytes TEXT NOT NULL,
            prev_block_hash VARCHAR(256),
            curr_block_hash VARCHAR(256),
            sender TEXT NOT NULL,
            receiver TEXT NOT NULL,
            amount TEXT NOT NULL,
            timestamp BIGINTEGER NOT NULL,
            signature TEXT NOT NULL,
            count INTEGER NOT NULL DEFAULT 1,
            UNIQUE(id, rand_bytes, prev_block_hash, curr_block_hash, sender, receiver, amount, timestamp, signature)
        )
        """;
}
