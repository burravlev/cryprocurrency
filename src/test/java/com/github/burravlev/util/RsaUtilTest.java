package com.github.burravlev.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

public class RsaUtilTest {
    @Test
    public void testPublicFromPrivate() {
        var pair = RsaUtil.generateKeyPair();
        var regenerated = RsaUtil.getPublic(pair.getPrivate());
        Assertions.assertEquals(pair.getPublic(), regenerated);
    }

    @Test
    public void testSign() {
        String data = "Test data";
        KeyPair pair = RsaUtil.generateKeyPair();
        String signed = RsaUtil.sign(data, pair.getPrivate());
        Assertions.assertTrue(RsaUtil.verifySign(data, signed, pair.getPublic()));
    }
}
