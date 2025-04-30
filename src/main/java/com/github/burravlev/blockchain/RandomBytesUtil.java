package com.github.burravlev.blockchain;

import java.util.Random;

abstract class RandomBytesUtil {
    private static final int RAND_BYTES_LENGTH = 20;

    static byte[] get() {
        byte[] bytes = new byte[RAND_BYTES_LENGTH];
        new Random().nextBytes(bytes);
        return bytes;
    }
}
