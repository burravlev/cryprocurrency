package com.github.burravlev.network;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
class NetworkRouter {
    private final Map<String, Consumer<JsonNode>> routes = new ConcurrentHashMap<>();

    void subscribe(String topic, Consumer<JsonNode> callback) {
        this.routes.put(topic, callback);
    }

    void handle(String topic, JsonNode data) {
        var callback = routes.get(topic);
        if (callback == null) {
            log.warn("no handler found: {}", topic);
            return;
        }
        callback.accept(data);
    }

    void unsubscribe(String topic) {
        routes.remove(topic);
    }

    Set<String> routes() {
        return routes.keySet();
    }
}
