package com.github.burravlev.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {
    @Test
    public void shouldConvertCamelToSnake() {
        String value = "camelCaseExample";
        Assertions.assertEquals("camel_case_example", StringUtils.camelToSnake(value));
    }
}
