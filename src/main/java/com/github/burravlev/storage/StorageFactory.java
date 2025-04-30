package com.github.burravlev.storage;

public class StorageFactory {

    public static Storage byFile(String filename) {
        return new SqLiteStorage(filename);
    }
}
