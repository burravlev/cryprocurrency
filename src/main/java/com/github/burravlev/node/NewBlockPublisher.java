package com.github.burravlev.node;

import com.github.burravlev.blockchain.Block;
import com.github.burravlev.blockchain.EventPublisher;
import com.github.burravlev.network.Node;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NewBlockPublisher implements EventPublisher {
    private final Node node;

    @Override
    public void newBlock(Block block) {
        node.sendToTopic(Topics.NEW_BLOCK, block);
    }
}
