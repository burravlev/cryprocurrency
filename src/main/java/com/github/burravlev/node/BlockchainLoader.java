package com.github.burravlev.node;

import com.github.burravlev.blockchain.Block;
import com.github.burravlev.network.Node;
import com.github.burravlev.util.WaitUtil;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class BlockchainLoader {
    private final AtomicBoolean loading = new AtomicBoolean(false);
    private final Node node;
    private final Consumer<List<Block>> onBlockReceived;

    public void load() {
        if (node.connectedPeersCount() == 0) {
            return;
        }
        loading.set(true);
        AtomicInteger counter = new AtomicInteger();
        String topic = UUID.randomUUID().toString();
        node.subscribe(topic, blocks -> {
            var list = new ArrayList<>(Arrays.asList(blocks));
            onBlockReceived.accept(list);
            counter.incrementAndGet();
        }, Block[].class);
        node.sendToTopic(Topics.GETBLOCKS, new GetBlockReq(topic));
        WaitUtil.waitWithTimeout(() -> counter.get() == node.connectedPeersCount(), 5);
        loading.set(false);
    }

    public boolean isLoading() {
        return loading.get();
    }
}
