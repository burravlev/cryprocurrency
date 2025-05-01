package com.github.burravlev.network;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Message {
    private Map<String, Object> headers = new HashMap<>();
    private JsonNode data;
}
