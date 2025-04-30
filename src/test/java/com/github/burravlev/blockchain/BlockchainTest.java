package com.github.burravlev.blockchain;

import com.github.burravlev.util.JsonSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@Slf4j
public class BlockchainTest extends AbstractChainTest {
    @Test
    @SneakyThrows
    public void testTransfersAndBalance() {
        var storage = testStorage();

        var sender = new Wallet();
        var receiver = new Wallet();

        Blockchain chain = new DefaultBlockchain(sender, storage, block -> {
        });
        chain.mineGenesis("data");
        String amount = "0.0001";
        chain.transfer(sender, receiver.getAddress(), amount);
        var balance = chain.getBalance(sender.getAddress());

        BigDecimal expectedBalance = new BigDecimal("19000000")
            .subtract(new BigDecimal(amount));
        Assertions.assertEquals(expectedBalance, balance);
        Assertions.assertEquals(1, chain.size());
        storage.close();
    }

    @Test
    public void blockShouldBeValidAfterSave() throws Exception {
        var storage = testStorage();
        var sender = new Wallet();
        var chain = new DefaultBlockchain(sender, storage, block -> {});
        chain.mineGenesis("Test data");
        var blocks = chain.getBlocks();
        var json = JsonSerializer.serialize(blocks.getFirst());
        var deserialized = JsonSerializer.deserialize(json, Block.class);
        Assertions.assertTrue(blocks.getFirst().isValid());
        Assertions.assertTrue(deserialized.isValid());
        storage.close();
    }
}
