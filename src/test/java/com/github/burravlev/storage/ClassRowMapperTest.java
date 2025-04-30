package com.github.burravlev.storage;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClassRowMapperTest extends AbstractStorageTest {
    @Test
    public void shouldMapByClass() {
        Storage storage = getTestStorage();
        storage.execute("""
            CREATE TABLE test(
                string VARCHAR(16),
                int1 INTEGER,
                int2 INTEGER,
                byte1 INTEGER,
                byte2 INTEGER,
                double1 INTEGER,
                double2 INTEGER,
                float1 INTEGER,
                float2 INTEGER
            )
            """);
        var expected = new TestClass()
            .setString("string")
            .setInt1(1)
            .setInt2(1)
            .setByte1((byte) 1)
            .setByte2((byte) 1)
            .setDouble1(1)
            .setDouble2(1d)
            .setFloat1(1f)
            .setFloat2(1f);

        storage.execute("""
                INSERT INTO test(
                    string,
                    int1,
                    int2,
                    byte1,
                    byte2,
                    double1,
                    double2,
                    float1,
                    float2
                ) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)""",
            expected.getString(),
            expected.getInt1(),
            expected.getInt2(),
            expected.getByte1(),
            expected.getByte2(),
            expected.getDouble1(),
            expected.getDouble2(),
            expected.getFloat1(),
            expected.getFloat2()
        );
        List<TestClass> results = storage.query("SELECT * FROM test", TestClass.class);
        Assertions.assertFalse(results.isEmpty());
        Assertions.assertEquals(expected, results.getFirst());
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @EqualsAndHashCode
    public static class TestClass {
        private String string;
        private int int1;
        private Integer int2;
        private byte byte1;
        private Byte byte2;
        private double double1;
        private Double double2;
        private float float1;
        private Float float2;
    }
}
