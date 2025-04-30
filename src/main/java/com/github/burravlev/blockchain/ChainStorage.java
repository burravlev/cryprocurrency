package com.github.burravlev.blockchain;

import java.util.List;

interface ChainStorage {
    void save(Block block);

    String getLastHash();

    int count();

    List<Block> getBlocks();
}
