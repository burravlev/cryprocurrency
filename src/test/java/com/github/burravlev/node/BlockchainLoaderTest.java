package com.github.burravlev.node;

import com.github.burravlev.network.Node;
import com.github.burravlev.blockchain.Block;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public class BlockchainLoaderTest {
    @Test
    @SneakyThrows
    public void shouldLoad() {
        Node node = Mockito.mock(Node.class);
        AtomicBoolean isCalled = new AtomicBoolean();
        AtomicReference<String> topicReference = new AtomicReference<>();
        Block expectedBlock = BlockchainTestData.getBlock("hash");
        doReturn(1)
            .when(node)
            .connectedPeersCount();

        Consumer<List<Block>> onBlockReceived = response -> {
            isCalled.set(true);
            Assertions.assertEquals(1, response.size());
            Assertions.assertEquals(expectedBlock, response.getFirst());
        };
        doAnswer(invocation -> {
            Consumer<Block[]> response = invocation.getArgument(1);
            response.accept(new Block[]{expectedBlock});
            return null;
        })
            .when(node)
            .subscribe(anyString(), any(), eq(Block[].class));

        doAnswer(invocation -> {
            topicReference.set(invocation.getArgument(0));
            return null;
        })
            .when(node)
            .sendToTopic(anyString(), any(GetBlockReq.class));

        var loader = new BlockchainLoader(node, onBlockReceived);
        loader.load();
        node.close();
        Assertions.assertTrue(isCalled.get());
        Assertions.assertNotNull(topicReference.get());
    }
}
