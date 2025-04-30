package com.github.burravlev.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public abstract class Base64Util {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static byte[] encode(String data) {
        return encode(data.getBytes(CHARSET));
    }

    public static byte[] encode(byte[] data) {
        return Base64.getUrlEncoder().encode(data);
    }

    public static String encodeToString(byte[] data) {
        return new String(encode(data), StandardCharsets.UTF_8);
    }

    public static String encodeToString(String data) {
        return new String(encode(data), StandardCharsets.UTF_8);
    }

    public static byte[] decode(String data) {
        return decode(data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(byte[] data) {
        return Base64.getUrlDecoder().decode(data);
    }

    public static String decodeToString(String data) {
        return new String(decode(data), StandardCharsets.UTF_8);
    }
}
