package com.github.burravlev.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public abstract class ResourceUtil {
    public static Path getAbsolutePath(String filename) {
        Path source = Paths.get(ResourceUtil.class.getResource("/").getPath());
        return Paths.get(source.toAbsolutePath() + "/" + filename);
    }

    public static String getAbsolutePathAsString(String filename) {
        return getAbsolutePath(filename).toAbsolutePath().toString();
    }

    public static boolean create(String filename) {
        try {
            var path = getAbsolutePath(filename);
            return path.toFile().createNewFile();
        } catch (Exception e) {
            log.warn("cannot create file: ", e);
            return false;
        }
    }

    public static boolean delete(String filename) {
        return getAbsolutePath(filename).toFile().delete();
    }

    public static void deleteOnExit(String filename) {
        getAbsolutePath(filename).toFile().deleteOnExit();
    }
}
