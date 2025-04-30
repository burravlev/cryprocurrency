package com.github.burravlev.network;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
class Peer implements AutoCloseable {
    private final PeerConnection connection;
    private final BiConsumer<Peer, Message> onMessage;
    private final Consumer<Peer> onDestroy;
    private final Set<String> subscriptions = new HashSet<>();

    void subscribe(String topic) {
        log.info("subscribe node {}: {}", connection.getAddress(), topic);
        subscriptions.add(topic);
    }

    void unsubscribe(String topic) {
        subscriptions.remove(topic);
    }

    @SneakyThrows
    void start() {
        NetworkExecutor.execute(this::startReading);
    }

    @SneakyThrows
    private void startReading() {
        while (true) {
            try {
                Message message = connection.read();
                if (message == null) {
                    continue;
                }
                onMessage.accept(this, message);
            } catch (Exception e) {
                log.error("Cannot process incoming message!");
            }
        }
    }

    void send(Message event) {
        if (event.getTopic() == null || subscriptions.contains(event.getTopic())) {
            connection.send(event);
        }
    }

    String getAddress() {
        return connection.getAddress();
    }

    @Override
    public void close() throws Exception {
        onDestroy.accept(this);
        connection.close();
    }
}
