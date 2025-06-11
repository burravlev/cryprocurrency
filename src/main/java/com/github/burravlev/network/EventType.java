package com.github.burravlev.network;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
enum EventType {
    UNDEF(-1),
    HANDSHAKE(1),
    NEW_PEER(2),
    MESSAGE(3),
    OK(6);

    private static final Map<Integer, EventType> EVENTS = new HashMap<>();

    static {
        for (EventType event : values()) {
            EVENTS.put(event.code, event);
        }
    }

    private final int code;

    public static EventType byCode(int code) {
        return EVENTS.getOrDefault(code, UNDEF);
    }
}
