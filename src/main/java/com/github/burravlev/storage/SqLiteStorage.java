package com.github.burravlev.storage;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

@Slf4j
class SqLiteStorage implements Storage {
    private final Connection connection;

    @SneakyThrows
    SqLiteStorage(String filepath) {
        connection = DriverManager.getConnection("jdbc:sqlite:" + filepath);
    }

    @SneakyThrows
    @Override
    public PreparedStatement prepareStatement(String query) {
        return connection.prepareStatement(query);
    }

    @SneakyThrows
    @Override
    public void execute(String query) {
        try (var preparedStatement = prepareStatement(query)) {
            preparedStatement.execute();
        }
    }

    @SneakyThrows
    @Override
    public void execute(String query, Object... args) {
        try (var stmt = prepareStatement(query)) {
            for (int i = 0; i < args.length; i++) {
                set(stmt, i + 1, args[i]);
            }
            stmt.execute();
        }
    }

    @Override
    @SneakyThrows
    public ResultSet executeQuery(String query) {
        var statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    @Override
    @SneakyThrows
    public <T> T executeQuery(String query, RowMapper<T> mapper) {
        var statement = connection.createStatement();
        var rs = statement.executeQuery(query);
        return mapper.map(rs);
    }

    @Override
    @SneakyThrows
    public ResultSet executeQuery(String query, Object... args) {
        try (var stmt = prepareStatement(query)) {
            for (int i = 0; i < args.length; i++) {
                set(stmt, i + 1, args[i]);
            }
            return stmt.executeQuery();
        }
    }

    @Override
    @SneakyThrows
    public <T> T executeQuery(String query, RowMapper<T> mapper, Object... args) {
        try (var stmt = prepareStatement(query)) {
            for (int i = 0; i < args.length; i++) {
                set(stmt, i + 1, args[i]);
            }
            return mapper.map(stmt.executeQuery());
        }
    }

    @SneakyThrows
    private static void set(PreparedStatement stmt, int index, Object arg) {
        Class<?> type = arg.getClass();
        if (type == Integer.class) {
            stmt.setInt(index, (Integer) arg);
        } else if (type == String.class) {
            stmt.setString(index, (String) arg);
        } else if (type == Long.class) {
            stmt.setLong(index, (Long) arg);
        } else {
            stmt.setString(index, arg.toString());
        }
    }

    @Override
    @SneakyThrows
    public void close() {
        connection.close();
    }

    @Override
    public <T> List<T> query(String sql, Class<T> type, Object... args) {
        return executeQuery(sql, new ClassRowMapper<>(type), args);
    }

    @Override
    public <T> T queryFirst(String sql, Class<T> type, Object... args) {
        return executeQuery(sql, new ClassRowMapper<>(type), args)
            .getFirst();
    }
}
