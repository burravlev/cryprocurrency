package com.github.burravlev.blockchain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ChainStorageRestorer implements BlockchainRestorer {
    private final TmpChainStorage tmpChainStorage;

    public void add(Block block) {
        if (block.isValid()) {
            tmpChainStorage.add(block);
        }
    }

    public void restore(int threshold) {
        log.info("restoring: {}", threshold);
        tmpChainStorage.flush(threshold);
    }

    @Override
    public int size() {
        return tmpChainStorage.size();
    }
}