package com.github.burravlev.node;

import com.github.burravlev.blockchain.Transaction;
import com.github.burravlev.network.Node;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class TransactionPublisher {
    private final Node node;

    void publish(Transaction tx) {
        node.sendToTopic(Topics.NEW_TX, tx);
    }
}
