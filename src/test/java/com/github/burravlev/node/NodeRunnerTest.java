package com.github.burravlev.node;

import com.github.burravlev.util.ResourceUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class NodeRunnerTest {
    @Test
    @SneakyThrows
    public void shouldRunNode() {
        String configPath = ResourceUtil.getAbsolutePathAsString("config.json");
        Thread.startVirtualThread(() -> {
            NodeRunner.start("--config", configPath);
        });
    }

    @Test
    @SneakyThrows
    public void runNodeWithoutConfig() {
        Thread.startVirtualThread(() -> {
            NodeRunner.start("-p", "9090");
        });
    }
}
