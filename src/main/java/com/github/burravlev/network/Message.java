package com.github.burravlev.network;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
public class Message {
    private Map<String, Object> headers = new HashMap<>();
    private JsonNode data;

    public Message addHeader(String key, Object value) {
        headers.put(key, value);
        return this;
    }

    public Object getHeader(String key) {
        return headers.get(key);
    }
}
