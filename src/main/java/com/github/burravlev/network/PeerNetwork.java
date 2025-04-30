package com.github.burravlev.network;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
class PeerNetwork {
    private final Map<String, Peer> PEERS = new ConcurrentHashMap<>();

    private final NodeIdentity identity;
    private final BiConsumer<Peer, Message> onMessage;
    private final Consumer<Peer> onDestroy = peer -> {
        log.info("destroying: {}", peer);
    };

    PeerNetwork(NodeIdentity identity, NetworkRouter router) {
        this.identity = identity;
        onMessage = new MessageHandler(router);
    }

    List<String> getAddresses() {
        return PEERS.values()
            .stream()
            .map(Peer::getAddress)
            .collect(Collectors.toList());
    }

    @SneakyThrows
    void add(PeerConnection connection) {
        if (connection.getAddress().equals(identity.getAddress())) return;
        Peer peer = new Peer(connection, onMessage, onDestroy);
        if (!PEERS.containsKey(peer.getAddress())) {
            PEERS.put(peer.getAddress(), peer);
            peer.start();
        } else {
            connection.close();
        }
    }

    void send(Message event) {
        for (var peer : PEERS.values()) {
            peer.send(event);
        }
    }

    int connectedPeersCount() {
        return PEERS.size();
    }
}
