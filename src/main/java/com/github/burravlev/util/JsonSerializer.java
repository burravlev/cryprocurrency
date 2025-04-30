package com.github.burravlev.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class JsonSerializer {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public static <T> T deserialize(String value, Class<T> type) {
        if (value == null) {
            return null;
        }
        return objectMapper.readValue(value, type);
    }

    @SneakyThrows
    public static String serialize(Object o) {
        if (o == null) {
            return null;
        }
        return objectMapper.writeValueAsString(o);
    }

    public static JsonNode serializeTree(Object o) {
        if (o == null) {
            return null;
        }
        return objectMapper.valueToTree(o);
    }

    @SneakyThrows
    public static <T> T fromTree(JsonNode json, Class<T> type) {
        if (json == null) {
            return null;
        }
        return objectMapper.treeToValue(json, type);
    }

    @SneakyThrows
    public static <T> T fromTree(JsonNode json, TypeReference<T> type) {
        if (json == null) {
            return null;
        }
        return objectMapper.treeToValue(json, type);
    }
}
