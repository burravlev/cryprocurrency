package com.github.burravlev.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.burravlev.util.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class MessageHandler implements BiConsumer<Peer, Message> {
    private final NetworkRouter router;

    @Override
    public void accept(Peer peer, Message message) {
        try {
            EventType type = EventType.byCode(message.getEvent());
            switch (type) {
                case NEW_PEER -> log.info("new peer: {}", message.getData());
                case MESSAGE -> handle(message.getTopic(), message.getData());
                case SUB -> subscribe(peer, message.getData());
                case UNSUB -> unsubscribe(peer, message.getData());
                case UNDEF -> log.warn("undefined message: {}", message);
                case OK -> log.warn("OK");
            }
        } catch (Exception e) {
            log.warn("invalid message received: ", e);
        }
    }

    private void subscribe(Peer peer, JsonNode data) {
        Set<String> topics = getTopics(data);
        for (String topic : topics) {
            peer.subscribe(topic);
        }
    }

    private void unsubscribe(Peer peer, JsonNode data) {
        Set<String> topics = getTopics(data);
        for (String topic : topics) {
            peer.unsubscribe(topic);
        }
    }

    private static Set<String> getTopics(JsonNode data) {
        return Arrays.stream(JsonSerializer.fromTree(data, String[].class))
            .collect(Collectors.toSet());
    }

    private void handle(String route, JsonNode data) {
        router.handle(route, data);
    }
}
