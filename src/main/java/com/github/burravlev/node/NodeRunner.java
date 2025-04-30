package com.github.burravlev.node;

import com.github.burravlev.blockchain.Wallet;
import com.github.burravlev.util.JsonSerializer;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NodeRunner {
    private NodeRunner() {}

    public static void start(String ...args) {
        NodeConfig config = parseConfig(args);
        NodeInstance runner = NodeInstance.run(config);
        InputReader reader = new InputReader(new Scanner(System.in), runner);
        reader.read();
    }

    private static NodeConfig parseConfig(String... args) {
        ArgumentParser parser = new ArgumentParser();
        Map<ArgumentParser.Argument, List<String>> parsed = parser.parse(args);
        NodeConfig config;
        if (parsed.containsKey(ArgumentParser.Argument.CONFIG)) {
            config = read(parsed.get(ArgumentParser.Argument.CONFIG).getFirst());
        } else {
            config = new NodeConfig();
            config.setKey(new Wallet().getPrivateKey());
            config.setStoragePath("app.db");
        }
        if (parsed.containsKey(ArgumentParser.Argument.PORT)) {
            config.setPort(Integer.parseInt(parsed.get(ArgumentParser.Argument.PORT).getFirst()));
        }
        return config;
    }

    @SneakyThrows
    private static NodeConfig read(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String json = reader.lines().collect(Collectors.joining("\n"));
            return JsonSerializer.deserialize(json, NodeConfig.class);
        }
    }
}
