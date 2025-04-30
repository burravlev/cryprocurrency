package com.github.burravlev.storage;

import com.github.burravlev.util.StringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.*;

@RequiredArgsConstructor
class ClassRowMapper<T> implements RowMapper<List<T>> {
    private final Class<T> type;

    @Override
    @SneakyThrows
    public List<T> map(ResultSet rs) {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            result.add(newInstance(rs));
        }
        return result;
    }

    @SneakyThrows
    private T newInstance(ResultSet rs) {
        T object = type.getDeclaredConstructor().newInstance();
        Map<String, FieldData> fields = getFields();
        for (FieldData data : fields.values()) {
            setValue(object, data, rs);
        }
        return object;
    }

    @SneakyThrows
    private void setValue(T object, FieldData data, ResultSet rs) {
        if (!hasColumn(rs, data.getSqlName())) return;
        Method method = type.getMethod(data.getSetterName(), data.getType());
        if (data.getType().equals(String.class)) {
            method.invoke(object, rs.getString(data.getSqlName()));
        } else if (data.getType().equals(Integer.class) ||
            data.getType().equals(int.class)) {
            method.invoke(object, rs.getInt(data.getSqlName()));
        } else if (data.getType().equals(Long.class) ||
            data.getType().equals(long.class)) {
            method.invoke(object, rs.getLong(data.getSqlName()));
        } else if (data.getType().equals(Double.class) ||
            data.getType().equals(double.class)) {
            method.invoke(object, rs.getDouble(data.getSqlName()));
        } else if (data.getType().equals(Float.class) ||
            data.getType().equals(float.class)) {
            method.invoke(object, rs.getFloat(data.getSqlName()));
        } else if (data.getType().equals(Byte.class) ||
            data.getType().equals(byte.class)) {
            method.invoke(object, rs.getByte(data.getSqlName()));
        }
    }

    @SneakyThrows
    private static boolean hasColumn(ResultSet rs, String columnName) {
        ResultSetMetaData metaData = rs.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            if (Objects.equals(columnName, metaData.getColumnName(i))) {
                return true;
            }
        }
        return false;
    }

    private Map<String, FieldData> getFields() {
        Map<String, FieldData> fieldsMap = new HashMap<>();
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            fieldsMap.put(field.getName(), new FieldData()
                .setSqlName(mapName(field.getName()))
                .setType(field.getType())
                .setSetterName(getSetter(field.getName()))
            );
        }
        return fieldsMap;
    }

    private static String mapName(String name) {
        return StringUtils.camelToSnake(name);
    }

    private static String getSetter(String fieldName) {
        return "set" + Character.toUpperCase(fieldName.charAt(0)) +
            fieldName.substring(1);
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    private static class FieldData {
        private String sqlName;
        private Class<?> type;
        private String setterName;
    }
}
