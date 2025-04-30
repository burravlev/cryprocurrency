package com.github.burravlev.node;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class Accumulator<T> {
    private final Map<T, Integer> counters = new ConcurrentHashMap<>();

    void increment(T value) {
        counters.put(value, counters.getOrDefault(value, 0) + 1);
    }

    T get() {
        int max = -1;
        T maxValue = null;
        for (Map.Entry<T, Integer> entry : counters.entrySet()) {
            if (max < entry.getValue()) {
                max = entry.getValue();
                maxValue = entry.getKey();
            }
        }
        return maxValue;
    }
}
