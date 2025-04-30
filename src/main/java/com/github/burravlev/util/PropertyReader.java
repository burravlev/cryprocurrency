package com.github.burravlev.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

@Slf4j
public abstract class PropertyReader {
    public static void load(String filename) {
        ResourceUtil.create(filename);
        File properties = ResourceUtil.getAbsolutePath(filename)
            .toFile();
        try (var is = new FileInputStream(properties);
             var reader = new BufferedReader(new InputStreamReader(is))) {
            loadToEnv(reader.lines());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private static void loadToEnv(Stream<String> data) {
        data.forEach(line -> {
            String[] parts = line.split("=");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                System.setProperty(key, value);
            }
        });
    }
}
