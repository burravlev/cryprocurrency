package com.github.burravlev;

import com.github.burravlev.node.NodeRunner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Runner {
    public static void main(String[] args) {
        NodeRunner.start(args);
    }
}
