package com.github.burravlev.util;

import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.function.Supplier;

public abstract class WaitUtil {
    @SneakyThrows
    public static void waitWithTimeout(Supplier<Boolean> condition,
                                       long timeoutSec) {
        var outTime = LocalDateTime.now().plusSeconds(timeoutSec);
        while (!condition.get()) {
            if (LocalDateTime.now().isAfter(outTime)) {
                return;
            }
        }
    }

    @SneakyThrows
    public static void waitUntil(Supplier<Boolean> condition,
                                 long timeoutSec) {
        var outTime = LocalDateTime.now().plusSeconds(timeoutSec);
        while (!condition.get()) {
            if (LocalDateTime.now().isAfter(outTime)) {
                throw new IllegalStateException("timeout after " + timeoutSec + " seconds!");
            }
        }
    }
}
