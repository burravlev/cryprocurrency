package com.github.burravlev.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.burravlev.util.JsonSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Slf4j
class TcpNode implements Node {
    private final NodeIdentity identity;
    private final PeerNetwork network;
    private final ServerSocket server;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final NetworkRouter router = new NetworkRouter();

    @SneakyThrows
    TcpNode(int port, NodeIdentity identity) {
        this.identity = identity;
        this.server = new ServerSocket(port);
        this.network = new PeerNetwork(identity, router);
        logNodeCreation();
    }

    @Override
    public void sendToTopic(String topic, Object o) {
        var event = new Message()
            .setData(JsonSerializer.serializeTree(o));
        event.getHeaders().put(Headers.EVENT_TYPE, EventType.MESSAGE.code());
        event.getHeaders().put(Headers.TOPIC, topic);
        network.send(event);
    }

    @Override
    public int connectedPeersCount() {
        return network.connectedPeersCount();
    }

    @Override
    public void subscribe(String topic, Consumer<JsonNode> callback) {
        var message = new Message()
            .setData(JsonSerializer.serializeTree(
                    Set.of(topic)
                )
            );
        message.getHeaders().put(
            Headers.EVENT_TYPE, EventType.SUB.code()
        );
        network.send(message);
        this.router.subscribe(topic, callback);
    }

    @Override
    public <T> void subscribe(String topic, Consumer<T> callback, Class<T> type) {
        subscribe(topic, json -> {
            try {
                T body = JsonSerializer.fromTree(json, type);
                callback.accept(body);
            } catch (Exception e) {
                log.error("error handling request");
            }
        });
    }

    @Override
    public void unsubscribe(String topic) {
        router.unsubscribe(topic);
    }

    @Override
    public String getHost() {
        return identity.getHost();
    }

    @Override
    public int getPort() {
        return identity.getPort();
    }

    @Override
    public String getId() {
        return identity.getId();
    }

    @Override
    @SneakyThrows
    public void start() {
        running.compareAndSet(false, true);
        NetworkExecutor.execute(() -> {
            while (running.get()) {
                try {
                    Socket socket = server.accept();
                    var connection = new TcpPeerConnection(socket, identity);
                    sendPeers(connection);
                    sendTopics(connection);
                    network.add(connection);
                } catch (SocketException e) {
                    return;
                } catch (Exception e) {
                    log.error("Cannot accept new connection: ", e);
                }
            }
        });
    }

    private void sendTopics(PeerConnection connection) {
        var topics = router.routes();
        var pack = new Message()
            .setData(JsonSerializer.serializeTree(topics));
        pack.getHeaders().put(Headers.EVENT_TYPE, EventType.SUB.code());
        connection.send(pack);
    }

    private void sendPeers(PeerConnection connection) {
        var nodes = network.getAddresses();
        var pack = new Message()
            .setData(JsonSerializer.serializeTree(nodes));
        pack.getHeaders().put(Headers.EVENT_TYPE, EventType.NEW_PEER.code());
        connection.send(pack);
    }

    @Override
    public Identity getIdentity() {
        return identity;
    }

    void connect(List<String> addresses) {
        for (String address : addresses) {
            var connection = new TcpPeerConnection(address, identity);
            var pack = connection.read(10);
            var nodes = JsonSerializer.fromTree(pack.getData(), String[].class);
            for (String node : nodes) {
                network.add(new TcpPeerConnection(node, identity));
            }
            network.add(connection);
        }
    }

    private void logNodeCreation() {
        log.info("created node [host: {}, port {}, id: {}]",
            getHost(), getPort(), getId());
    }

    @Override
    @SneakyThrows
    public void close() {
        running.set(false);
        server.close();
    }
}
