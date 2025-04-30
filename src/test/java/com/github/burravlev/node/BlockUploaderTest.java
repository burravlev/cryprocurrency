package com.github.burravlev.node;

import com.github.burravlev.blockchain.Block;
import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.network.Node;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static com.github.burravlev.node.BlockchainTestData.getBlock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BlockUploaderTest {
    @Test
    @SneakyThrows
    public void shouldUploadBlock() {
        Blockchain blockchain = Mockito.mock(Blockchain.class);
        Node node = Mockito.mock(Node.class);
        String expectedTopic = UUID.randomUUID().toString();
        GetBlockReq expectedRequest = new GetBlockReq()
            .setTopic(expectedTopic);

        doAnswer(invocation -> {
            Consumer<GetBlockReq> consumer = invocation.getArgument(1);
            consumer.accept(expectedRequest);
            return null;
        })
            .when(node)
            .subscribe(eq(Topics.GETBLOCKS), any(), eq(GetBlockReq.class));
        Block expectedBlock = getBlock("");
        var expectedResponse = List.of(expectedBlock);
        doReturn(List.of(expectedBlock))
            .when(blockchain)
            .getBlocks();

        new BlockUploader(node, blockchain);

        verify(node, times(1))
            .subscribe(anyString(), any(), any());
        verify(node, times(1))
            .sendToTopic(eq(expectedTopic), eq(expectedResponse));
        node.close();
    }
}
