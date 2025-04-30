package com.github.burravlev.blockchain;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class WalletTest {
    @Test
    public void creation() {
        var wallet1 = new Wallet();
        var wallet2 = new Wallet();
        Assertions.assertNotEquals(wallet1, wallet2);
        log.info("wallet1: {}\n{}", wallet1.getAddress(), wallet1.getPrivateKey());
        log.info("wallet2: {}\n{}", wallet2.getAddress(), wallet2.getPrivateKey());
        Assertions.assertNotEquals(wallet1.getAddress(), wallet2.getAddress());
    }
}
