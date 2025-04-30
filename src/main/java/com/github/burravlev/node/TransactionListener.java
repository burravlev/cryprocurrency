package com.github.burravlev.node;

import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.blockchain.Transaction;
import com.github.burravlev.network.Node;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class TransactionListener {
    public TransactionListener(Node node,
                               Blockchain blockchain) {
        node.subscribe(Topics.NEW_TX, tx -> {
            log.info("new transaction: {}", tx);
            blockchain.addTx(tx);
        }, Transaction.class);
    }
}
