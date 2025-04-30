package com.github.burravlev.storage;

import com.github.burravlev.util.ResourceUtil;

import java.util.UUID;

public class AbstractStorageTest {
    protected Storage getTestStorage() {
        String filename = getFileName();
        String path = ResourceUtil.getAbsolutePath(filename)
            .toAbsolutePath().toString();
        ResourceUtil.create(filename);
        ResourceUtil.deleteOnExit(filename);
        return StorageFactory.byFile(path);
    }

    protected static String getFileName() {
        return "test-" + UUID.randomUUID() + ".db";
    }
}
