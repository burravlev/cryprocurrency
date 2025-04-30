package com.github.burravlev.node;

import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.network.Node;
import com.github.burravlev.util.WaitUtil;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class BalanceLoader {
    private final Node node;
    private final Blockchain blockchain;

    public BigDecimal getUserBalance(String address) {
        String topic = UUID.randomUUID().toString();
        Accumulator<BigDecimal> accumulator = new Accumulator<>();
        accumulator.increment(blockchain.getBalance(address));
        AtomicInteger counter = new AtomicInteger();
        node.subscribe(topic, balance -> {
            accumulator.increment(new BigDecimal(balance.getAmount()));
            counter.incrementAndGet();
        }, BalanceResponse.class);
        node.sendToTopic(Topics.GETBALANCE, new BalanceRequest()
            .setTopic(topic)
            .setAddress(address)
        );
        WaitUtil.waitWithTimeout(() -> counter.get() == node.connectedPeersCount(), 2);
        node.unsubscribe(topic);
        return accumulator.get();
    }
}
