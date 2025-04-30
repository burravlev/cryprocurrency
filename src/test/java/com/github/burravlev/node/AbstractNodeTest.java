package com.github.burravlev.node;

import com.github.burravlev.storage.Storage;
import com.github.burravlev.storage.StorageFactory;
import com.github.burravlev.util.ResourceUtil;

public class AbstractNodeTest {
    protected Storage testStorage(String filename) {
        String path = ResourceUtil.getAbsolutePathAsString(filename);
        ResourceUtil.create(filename);
        ResourceUtil.deleteOnExit(filename);
        return StorageFactory.byFile(path);
    }
}
