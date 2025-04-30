package com.github.burravlev.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class AesUtil {
    @SneakyThrows
    public static SecretKey getSecret() {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        return generator.generateKey();
    }

    public static String toBase64(SecretKey key) {
        return Base64Util.encodeToString(key.getEncoded());
    }

    public static SecretKey fromBase64(String key) {
        byte[] decoded = Base64Util.decode(key);
        return new SecretKeySpec(decoded, 0, decoded.length, "AES");
    }

    @SneakyThrows
    public static String encrypt(String data, SecretKey key) {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64Util.encodeToString(encrypted);
    }

    @SneakyThrows
    public static String decrypt(String data, SecretKey key) {
        byte[] fromBase64 = Base64Util.decode(data);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(fromBase64);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
