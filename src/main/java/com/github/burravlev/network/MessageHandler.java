package com.github.burravlev.network;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
class MessageHandler implements BiConsumer<Peer, Message> {
    private final NetworkRouter router;

    @Override
    public void accept(Peer peer, Message message) {
        try {
            EventType type = EventType.byCode((int) message.getHeaders().get(Headers.EVENT_TYPE));
            switch (type) {
                case NEW_PEER -> log.info("new peer: {}", message.getData());
                case MESSAGE -> handle((String) message.getHeaders().get(Headers.TOPIC), message.getData());
                case UNDEF -> log.warn("undefined message: {}", message);
                case OK -> log.warn("OK");
            }
        } catch (Exception e) {
            log.warn("invalid message received: ", e);
        }
    }

    private void handle(String route, JsonNode data) {
        router.handle(route, data);
    }
}
