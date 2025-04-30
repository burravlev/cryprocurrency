package com.github.burravlev.node;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ArgumentParser {
    @RequiredArgsConstructor
    enum Argument {
        PORT("-p", 1),
        NEW_WALLET("-new", 0),
        SYNC("--sync", 0),
        CONFIG("--config", 1);

        private final String argName;
        private final int argsCount;
    }

    public Map<Argument, List<String>> parse(String... args) {
        var parsed = new HashMap<Argument, List<String>>();
        Argument previous = null;
        for (String arg : args) {
            Argument current = parse(arg);
            parsed.putIfAbsent(current, new ArrayList<>());
            if (current != null) {
                previous = current;
            } else {
                var values = parsed.getOrDefault(previous, new ArrayList<>());
                values.add(arg);
                parsed.put(previous, values);
            }
        }
        parsed.remove(null);
        for (var entry : parsed.entrySet()) {
            Argument argument = entry.getKey();
            if (argument.argsCount != entry.getValue().size()) {
                throw new IllegalStateException("");
            }
        }
        return parsed;
    }

    private Argument parse(String arg) {
        for (Argument argument : Argument.values()) {
            if (arg.equals(argument.argName)) return argument;
        }
        return null;
    }
}
