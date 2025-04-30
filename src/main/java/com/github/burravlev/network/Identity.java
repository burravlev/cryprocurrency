package com.github.burravlev.network;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Identity {
    char DELIMITER = '/';

    String getHost();
    int getPort();
    String getId();

    @JsonIgnore
    default String getAddress() {
        return getHost() + DELIMITER + getPort() + DELIMITER + getId();
    }
}
