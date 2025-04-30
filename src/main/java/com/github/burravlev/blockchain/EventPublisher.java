package com.github.burravlev.blockchain;

public interface EventPublisher {
    void newBlock(Block block);
}
