package com.github.burravlev.node;

import com.github.burravlev.blockchain.BlockchainRestorer;
import com.github.burravlev.network.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChainRestorer {
    private final Node node;
    private final BlockchainLoader blockchainLoader;
    private final BlockchainRestorer restorer;

    public ChainRestorer(Node node, BlockchainRestorer restorer) {
        this.node = node;
        this.restorer = restorer;
        this.blockchainLoader = new BlockchainLoader(node, blocks -> {
            for (var block : blocks) {
                restorer.add(block);
            }
        });
    }

    public void restore() {
        blockchainLoader.load();
        while (blockchainLoader.isLoading()) {
        }
        log.info("loaded: {}", restorer.size());
        restorer.restore(node.connectedPeersCount());
    }
}
