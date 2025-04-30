package com.github.burravlev.blockchain;

import java.util.Collection;

class BlockMiner {
    public Block mine(String prevHash,
                      Wallet miner,
                      int difficulty,
                      Collection<Transaction> transactions) {
        return new Block(prevHash, miner, difficulty, transactions);
    }
}
