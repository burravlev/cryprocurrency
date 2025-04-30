package com.github.burravlev.storage;

import java.sql.ResultSet;

public interface RowMapper<T> {
    T map(ResultSet rs);
}
