package com.github.burravlev.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Base64UtilTest {
    @Test
    public void shouldSuccessfullyEncodeAndDecode() {
        String value = "test";
        String encoded = Base64Util.encodeToString(value);
        Assertions.assertEquals(value, Base64Util.decodeToString(encoded));
    }
}
