package com.github.burravlev.blockchain;

public interface BlockchainRestorer {
    void add(Block block);
    void restore(int threshold);
    int size();
}
