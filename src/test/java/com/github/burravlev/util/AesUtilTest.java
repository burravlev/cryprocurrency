package com.github.burravlev.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

@Slf4j
public class AesUtilTest {
    @Test
    public void shouldGetKeyEncodeAndDecode() {
        SecretKey key = AesUtil.getSecret();
        SecretKey decoded = AesUtil.fromBase64(AesUtil.toBase64(key));
        Assertions.assertEquals(key, decoded);
    }

    @Test
    public void shouldEncryptAndDecrypt() {
        String data = "data";
        SecretKey key = AesUtil.getSecret();
        String encrypted = AesUtil.encrypt(data, key);
        log.info("encrypted: {}", encrypted);
        String decrypted = AesUtil.decrypt(encrypted, key);
        Assertions.assertEquals(data, decrypted);
    }
}
