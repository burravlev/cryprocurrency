package com.github.burravlev.node;

import com.github.burravlev.blockchain.Block;
import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.network.Node;

public class NewBlockListener {
    public NewBlockListener(Blockchain blockchain, Node node) {
        node.subscribe(Topics.NEW_BLOCK, blockchain::addBlock, Block.class);
    }
}
