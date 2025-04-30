package com.github.burravlev.network;

import com.github.burravlev.util.JsonSerializer;
import com.github.burravlev.util.WaitUtil;
import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class NodeTest {

    @Test
    @SneakyThrows
    public void testTwoPeersNet() {
        Node node1 = new NodeBuilder()
            .build();
        node1.start();
        Node node2 = new NodeBuilder()
            .addresses(List.of(
                node1.getIdentity().getAddress()
            ))
            .build();
        node2.start();
        Assertions.assertEquals(1, node1.connectedPeersCount());
        Assertions.assertEquals(1, node2.connectedPeersCount());
        node1.close();
        node2.close();
    }

    @Test
    @SneakyThrows
    public void testNodeStart() {
        Node node1 = new NodeBuilder().build();
        node1.start();
        Node node2 = new NodeBuilder()
            .addresses(List.of(node1.getIdentity().getAddress()))
            .build();
        node2.start();
        Node node3 = new NodeBuilder()
            .addresses(List.of(node2.getIdentity().getAddress()))
            .build();
        node3.start();

        String topic = "/test";
        var data = getData();
        AtomicBoolean isCalled = new AtomicBoolean(false);
        node1.subscribe(topic, json -> {
            isCalled.set(true);
            Assertions.assertEquals(data, JsonSerializer.fromTree(json, TestData.class));
        });
        Thread.sleep(100);
        node3.sendToTopic(topic, data);
        WaitUtil.waitUntil(isCalled::get, 5);
        Assertions.assertTrue(isCalled.get());
        Assertions.assertEquals(2, node1.connectedPeersCount());
        Assertions.assertEquals(2, node2.connectedPeersCount());
        Assertions.assertEquals(2, node3.connectedPeersCount());
        node3.close();
        node2.close();
        node1.close();
    }

    @Test
    @SneakyThrows
    public void testSubscriptions() {
        Node node1 = new NodeBuilder().build();
        node1.start();
        Node node2 = new NodeBuilder()
            .addresses(List.of(node1.getIdentity().getAddress()))
            .build();
        node2.start();

        node1.subscribe("topic", json -> {});

        node1.close();
        node2.close();
    }

    private static TestData getData() {
        return new TestData("test", 10);
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestData {
        private String name;
        private int value;
    }
}
