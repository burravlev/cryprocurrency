package com.github.burravlev.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PropertyReaderTest {
    @Test
    public void shouldLoad() {
        PropertyReader.load("test.properties");

        Assertions.assertEquals("localhost", System.getProperty("HOST"));
        Assertions.assertEquals("8080", System.getProperty("PORT"));
    }
}
