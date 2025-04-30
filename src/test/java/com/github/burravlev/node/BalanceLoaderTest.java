package com.github.burravlev.node;

import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.network.Node;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BalanceLoaderTest {
    @Test
    @SneakyThrows
    public void shouldGetBalance() {
        BigDecimal expectedBalance = new BigDecimal("10.001");
        String address = UUID.randomUUID().toString();

        Node node = Mockito.mock(Node.class);
        Blockchain blockchain = Mockito.mock(Blockchain.class);
        int nodes = 10;
        AtomicReference<String> topic = new AtomicReference<>();
        doReturn(nodes).when(node).connectedPeersCount();
        doAnswer(invocation -> {
            topic.set(invocation.getArgument(0));
            Consumer<BalanceResponse> consumer = invocation.getArgument(1);
            for (int i = 0; i < nodes; i++) {
                consumer.accept(new BalanceResponse()
                    .setAmount(expectedBalance.toPlainString())
                );
            }
            Assertions.assertEquals(BalanceResponse.class, invocation.getArgument(2));
            return null;
        }).when(node).subscribe(anyString(), any(), any());
        doReturn(expectedBalance).when(blockchain)
            .getBalance(address);
        doNothing().when(node).sendToTopic(eq(Topics.GETBLOCKS), eq(new BalanceRequest()
            .setTopic(topic.get())
            .setAddress(address))
        );
        BalanceLoader loader = new BalanceLoader(node, blockchain);
        BigDecimal balance = loader.getUserBalance(address);
        verify(node, times(1))
            .unsubscribe(eq(topic.get()));
        Assertions.assertEquals(expectedBalance, balance);
        node.close();
    }
}
