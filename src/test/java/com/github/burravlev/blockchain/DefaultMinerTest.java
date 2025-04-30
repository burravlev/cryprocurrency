package com.github.burravlev.blockchain;

import com.github.burravlev.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

public class DefaultMinerTest {
    @Test
    public void shouldMine() {
        TxStorage txStorage = Mockito.mock(TxStorage.class);
        ChainStorage chainStorage = Mockito.mock(ChainStorage.class);
        DifficultyCalculator calculator = Mockito.mock(DifficultyCalculator.class);
        EventPublisher publisher = Mockito.mock(EventPublisher.class);
        Wallet wallet = Mockito.mock(Wallet.class);
        BlockMiner blockMiner = Mockito.mock(BlockMiner.class);
        Miner miner = new DefaultMiner(
            txStorage,
            chainStorage,
            calculator,
            publisher,
            wallet,
            blockMiner
        );

        doReturn(TestData.getTransactions())
            .when(txStorage)
            .findNotAccepted();
        doReturn(TestData.HASH)
            .when(chainStorage)
            .getLastHash();
        Block block = TestData.getBlock();
        doReturn(block)
            .when(blockMiner)
            .mine(eq(TestData.HASH), any(), anyInt(), anyCollection());
        doNothing()
            .when(txStorage)
            .markAccepted(any(), eq(block.getHash()));
        doNothing()
            .when(chainStorage)
            .save(eq(block));
        doNothing()
            .when(publisher)
            .newBlock(eq(block));

        miner.mine();
    }
}
