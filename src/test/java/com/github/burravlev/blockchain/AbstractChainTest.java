package com.github.burravlev.blockchain;

import com.github.burravlev.storage.Storage;
import com.github.burravlev.storage.StorageFactory;
import com.github.burravlev.util.ResourceUtil;

import java.util.UUID;

public class AbstractChainTest {
    protected Storage testStorage() {
        String filename = UUID.randomUUID() + ".db";
        String path = ResourceUtil.getAbsolutePathAsString(filename);
        ResourceUtil.create(filename);
        ResourceUtil.deleteOnExit(filename);
        return StorageFactory.byFile(path);
    }
}
