package com.github.burravlev.network;

import com.github.burravlev.util.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class MessageSerializerTest {
    @Test
    public void shouldSerializeAndDeserialize() {
        Message event = new Message()
            .setData(JsonSerializer.serializeTree(
                new PublicIdentity()
                    .setHost("localhost")
                    .setId("id")
                    .setPort(9090)
            ));
        event.getHeaders().put(Headers.EVENT_TYPE, EventType.HANDSHAKE.code());
        String json = JsonSerializer.serialize(event);
        log.info(json);
        Message received = JsonSerializer.deserialize(json, Message.class);
        Assertions.assertEquals(event, received);
    }
}
