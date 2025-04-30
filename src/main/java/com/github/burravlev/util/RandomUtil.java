package com.github.burravlev.util;

import java.util.Random;

public class RandomUtil {
    public static byte[] randomArray(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }
}
