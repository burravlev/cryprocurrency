package com.github.burravlev.network;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract class NetworkExecutor {
    private static final ExecutorService executor =
        Executors.newVirtualThreadPerTaskExecutor();

    static void execute(Runnable runnable) {
        executor.execute(runnable);
    }
}
