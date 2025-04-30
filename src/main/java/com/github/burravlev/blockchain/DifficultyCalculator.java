package com.github.burravlev.blockchain;

public interface DifficultyCalculator {
    default int calc() {
        return 1;
    }
}
