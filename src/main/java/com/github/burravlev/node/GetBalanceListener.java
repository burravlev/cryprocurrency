package com.github.burravlev.node;

import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.network.Node;

public class GetBalanceListener {
    public GetBalanceListener(Node node, Blockchain blockchain) {
        node.subscribe(Topics.GETBALANCE, request -> {
            node.sendToTopic(request.getTopic(), new BalanceResponse()
                .setAmount(blockchain.getBalance(request.getAddress()).toString())
            );
        }, BalanceRequest.class);
    }
}
