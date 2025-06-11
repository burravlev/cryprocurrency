package com.github.burravlev.network;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
class Peer implements AutoCloseable {
    private final PeerConnection connection;
    private final BiConsumer<Peer, Message> onMessage;
    private final Consumer<Peer> onDestroy;

    @SneakyThrows
    void start() {
        NetworkExecutor.execute(this::startReading);
    }

    @SneakyThrows
    private void startReading() {
        while (true) {
            try {
                Message message = connection.read();
                if (message != null) {
                    onMessage.accept(this, message);
                }
            } catch (Exception e) {
                log.error("Cannot process incoming message!");
            }
        }
    }

    void send(Message event) {
        connection.send(event);
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
