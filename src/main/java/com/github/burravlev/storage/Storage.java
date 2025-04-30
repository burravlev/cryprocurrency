package com.github.burravlev.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public interface Storage extends AutoCloseable {
    PreparedStatement prepareStatement(String query);

    void execute(String query);

    void execute(String query, Object... args);

    ResultSet executeQuery(String query);

    <T> T executeQuery(String query, RowMapper<T> mapper);

    ResultSet executeQuery(String query, Object... args);

    <T> T executeQuery(String query, RowMapper<T> mapper, Object... args);

    <T> List<T> query(String sql, Class<T> type, Object... args);

    default <T> T queryFirst(String sql, Class<T> type, Object... args) {
        return null;
    }
}
