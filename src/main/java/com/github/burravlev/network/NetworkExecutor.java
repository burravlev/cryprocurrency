package com.github.burravlev.network;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
class NetworkExecutor {
    private static final ExecutorService executor =
        Executors.newVirtualThreadPerTaskExecutor();

    void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
