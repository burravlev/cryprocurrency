package com.github.burravlev.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ResourceUtilTest {
    @Test
    public void shouldCreateFile() {
        String filename = getFilename();
        ResourceUtil.getAbsolutePath(filename).toFile().deleteOnExit();
        boolean created = ResourceUtil.create(filename);
        Assertions.assertTrue(created);
    }

    @Test
    public void shouldThrowOnExistedFile() {
        String filename = getFilename();
        ResourceUtil.getAbsolutePath(filename).toFile().deleteOnExit();
        boolean created = ResourceUtil.create(filename);
        Assertions.assertTrue(created);
        created = ResourceUtil.create(filename);
        Assertions.assertFalse(created);
    }

    private static String getFilename() {
        return "test-" + UUID.randomUUID();
    }
}
