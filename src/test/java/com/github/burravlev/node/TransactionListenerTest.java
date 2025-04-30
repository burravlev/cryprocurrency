package com.github.burravlev.node;

import com.github.burravlev.blockchain.Blockchain;
import com.github.burravlev.blockchain.Transaction;
import com.github.burravlev.blockchain.Wallet;
import com.github.burravlev.network.Node;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TransactionListenerTest {
    @Test
    @SneakyThrows
    public void shouldListenToNewTransactions() {
        String hash = "hash";
        Wallet from = new Wallet();
        Wallet to = new Wallet();
        String amount = "0.01";
        Node node = Mockito.mock(Node.class);
        Blockchain blockchain = Mockito.mock(Blockchain.class);
        Transaction tx = new Transaction(
            hash, from, to.getAddress(), amount
        );
        doAnswer(invocation -> {
            Consumer<Transaction> consumer = invocation.getArgument(1);
            consumer.accept(tx);
            return null;
        })
            .when(node)
            .subscribe(eq(Topics.NEW_TX), any(), any());
        new TransactionListener(node, blockchain);
        verify(blockchain, times(1)).addTx(tx);
        node.close();
    }
}
