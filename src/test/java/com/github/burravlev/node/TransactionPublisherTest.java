package com.github.burravlev.node;

import com.github.burravlev.network.Node;
import com.github.burravlev.blockchain.Transaction;
import com.github.burravlev.blockchain.Wallet;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TransactionPublisherTest {
    @Test
    @SneakyThrows
    public void shouldPublish() {
        String hash = "HASH";
        Wallet from = new Wallet();
        Wallet to = new Wallet();
        String amount = "0.011";

        Node node = Mockito.mock(Node.class);
        TransactionPublisher publisher = new TransactionPublisher(node);

        var tx = new Transaction(
            hash, from, to.getAddress(), amount
        );

        publisher.publish(tx);
        verify(node, times(1))
            .sendToTopic(eq(Topics.NEW_TX), eq(tx));
        node.close();
    }
}
