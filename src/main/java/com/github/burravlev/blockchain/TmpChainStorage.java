package com.github.burravlev.blockchain;

interface TmpChainStorage extends StorageInitializer {
    void add(Block block);
    void flush(int threshold);
    int size();
}
