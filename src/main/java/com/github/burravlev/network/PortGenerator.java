package com.github.burravlev.network;

import java.util.Random;

abstract class PortGenerator {
    static int generate() {
        return 8000 + new Random().nextInt(2001);
    }
}
