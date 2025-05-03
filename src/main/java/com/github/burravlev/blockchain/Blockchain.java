package com.github.burravlev.blockchain;

import java.math.BigDecimal;
import java.util.List;

public interface Blockchain {
    void mineGenesis(String data);

    Transaction transfer(Wallet from, String to, String amount);

    void addTx(Transaction tx);

    BigDecimal getBalance(String address);

    BlockchainRestorer restorer();

    List<Block> getBlocks();

    int size();

    void addBlock(Block block);
}
