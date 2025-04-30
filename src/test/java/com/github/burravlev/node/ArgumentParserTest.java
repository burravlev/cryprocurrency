package com.github.burravlev.node;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArgumentParserTest {
    @Test
    public void shouldParseArguments() {
        ArgumentParser parser = new ArgumentParser();
        var result = parser.parse("-p", "8080", "--config", "config");
        Assertions.assertTrue(result.containsKey(ArgumentParser.Argument.PORT));
        Assertions.assertTrue(result.containsKey(ArgumentParser.Argument.CONFIG));
        Assertions.assertEquals(1, result.get(ArgumentParser.Argument.CONFIG).size());
        Assertions.assertEquals(1, result.get(ArgumentParser.Argument.PORT).size());
    }

    @Test
    public void shouldThrow() {
        ArgumentParser parser = new ArgumentParser();
        Assertions.assertThrows(IllegalStateException.class, () -> parser.parse("-p"));
    }
}
