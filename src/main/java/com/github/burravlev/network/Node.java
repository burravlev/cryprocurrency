package com.github.burravlev.network;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.SneakyThrows;

import java.util.function.Consumer;

public interface Node extends AutoCloseable {

    void subscribe(String topic, Consumer<JsonNode> callback);

    <T> void subscribe(String topic, Consumer<T> callback, Class<T> type);

    void unsubscribe(String topic);

    String getHost();

    int getPort();

    String getId();

    void sendToTopic(String topic, Object o);

    int connectedPeersCount();

    @SneakyThrows
    void start();

    Identity getIdentity();
}
