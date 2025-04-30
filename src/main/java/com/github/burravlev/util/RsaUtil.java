package com.github.burravlev.util;

import lombok.SneakyThrows;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

public abstract class RsaUtil {
    @SneakyThrows
    public static String sign(String data, PrivateKey privateKey) {
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(bytes);

        byte[] signature = privateSignature.sign();
        return Base64Util.encodeToString(signature);
    }

    @SneakyThrows
    public static String sign(String data, String privateKey) {
        return sign(data, privateFromBase64Encoded(privateKey));
    }

    @SneakyThrows
    public static boolean verifySign(String data, String signature, PublicKey publicKey) {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        byte[] decodedSignature = Base64Util.decode(signature.getBytes(StandardCharsets.UTF_8));
        publicSignature.update(data.getBytes(StandardCharsets.UTF_8));
        return publicSignature.verify(decodedSignature);
    }

    @SneakyThrows
    public static boolean verifySign(String data, String signature, String publicKey) {
        return verifySign(data, signature, publicFromBase64Encoded(publicKey));
    }

    @SneakyThrows
    public static KeyPair getNewKeyPair() {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }

    public static String keyToBase64Encoded(Key key) {
        byte[] bytes = key.getEncoded();
        return Base64Util.encodeToString(bytes);
    }

    public static PublicKey publicFromBase64Encoded(String value) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] publicKeyBytes = Base64Util.decode(value.getBytes(StandardCharsets.UTF_8));
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }

    @SneakyThrows
    public static PrivateKey privateFromBase64Encoded(String value) {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] privateKeyBytes = Base64Util.decode(value.getBytes(StandardCharsets.UTF_8));
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
    }

    private static final int KEY_SIZE = 2048;

    @SneakyThrows
    public static KeyPair generateKeyPair() {
        var secureRandom = new SecureRandom();
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(KEY_SIZE, secureRandom);
        return generator.generateKeyPair();
    }

    @SneakyThrows
    public static Key publicFromBase64(byte[] base64) {
        byte[] publicKeyBytes = Base64Util.decode(base64);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(publicKeySpec);
    }

    @SneakyThrows
    public static Key publicFromBase64(String publicKeyBytes) {
        return publicFromBase64(publicKeyBytes.getBytes(StandardCharsets.UTF_8));
    }

    public static String publicToBase64(Key key) {
        return Base64Util.encodeToString(key.getEncoded());
    }

    @SneakyThrows
    public static String encrypt(String data, Key publicKey) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8), publicKey);
    }

    @SneakyThrows
    public static String encrypt(byte[] data, Key publicKey) {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64Util.encodeToString(cipher.doFinal(data));
    }

    @SneakyThrows
    public static String decrypt(String data, Key privateKey) {
        byte[] bytes = Base64Util.decode(data);
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(decryptCipher.doFinal(bytes), StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static PublicKey getPublic(PrivateKey privateKey) {
        var rsaPrivateKey = (RSAPrivateCrtKey) privateKey;
        var spec = new RSAPublicKeySpec(rsaPrivateKey.getModulus(), rsaPrivateKey.getPublicExponent());
        var factory = KeyFactory.getInstance("RSA");
        return factory.generatePublic(spec);
    }

    @SneakyThrows
    public static PublicKey getPublic(Key privateKey) {
        return getPublic((PrivateKey) privateKey);
    }

    public static String getPublic(String privateKey) {
        var publicKey = getPublic(privateFromBase64Encoded(privateKey));
        return publicToBase64(publicKey);
    }
}
