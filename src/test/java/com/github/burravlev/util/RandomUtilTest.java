package com.github.burravlev.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class RandomUtilTest {
    @Test
    public void testRandom() {
        int length = 10;
        byte[] arr1 = RandomUtil.randomArray(length);
        byte[] arr2 = RandomUtil.randomArray(length);
        boolean equal = Arrays.equals(arr1, arr2);
        Assertions.assertFalse(equal);
        Assertions.assertEquals(length, arr2.length);
        Assertions.assertEquals(length, arr1.length);
    }
}
