package com.github.burravlev.storage;

import com.github.burravlev.util.ResourceUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class StorageTest extends AbstractStorageTest {

    @Test
    @SneakyThrows
    public void shouldSaveToFileAndRead() {
        String filename = getFileName();
        Path path = ResourceUtil.getAbsolutePath(filename);
        ResourceUtil.create(filename);
        ResourceUtil.deleteOnExit(filename);
        Storage storage = StorageFactory.byFile(path.toAbsolutePath().toString());
        storage.execute("CREATE TABLE test(id INTEGER, value VARCHAR(32))");

        int expectedId = 10;
        String expectedValue = "value";

        storage.execute("INSERT INTO test(id, value) VALUES(?, ?)", expectedId, expectedValue);

        ResultSet rs = storage.executeQuery("SELECT * FROM test");
        Assertions.assertTrue(rs.next());
        TestEntity result = storage.executeQuery("SELECT * FROM test LIMIT 1", set -> {
            try {
                return new TestEntity()
                    .setId(set.getInt("id"))
                    .setValue(set.getString("value"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Assertions.assertEquals(expectedId, result.getId());
        Assertions.assertEquals(expectedValue, result.getValue());
    }

    @Test
    @SneakyThrows
    public void shouldCompareStrings() {
        try (var storage = getTestStorage()) {
            storage.execute("CREATE TABLE test(id INTEGER, value VARCHAR(32))");
            storage.execute("INSERT INTO test(id, value) VALUES(10, 'name')");
            var rs = storage.executeQuery("SELECT * FROM test WHERE value > ?", "");
            Assertions.assertThrows(SQLException.class, () -> rs.getString("value"));
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @EqualsAndHashCode
    private static class TestEntity {
        private int id;
        private String value;
    }
}
