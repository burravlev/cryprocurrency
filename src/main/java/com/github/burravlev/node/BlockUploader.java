package com.github.burravlev.node;

import com.github.burravlev.blockchain.Block;
import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.network.Node;

import java.util.List;

public class BlockUploader {
    private final Node node;
    private final Blockchain blockchain;

    public BlockUploader(Node node, Blockchain blockchain) {
        this.node = node;
        this.blockchain = blockchain;
        subscribe();
    }

    private void subscribe() {
        node.subscribe(Topics.GETBLOCKS, request -> {
            String topic = request.getTopic();
            List<Block> blocks = blockchain.getBlocks();
            node.sendToTopic(topic, blocks);
        }, GetBlockReq.class);
    }
}
