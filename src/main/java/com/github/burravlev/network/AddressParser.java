package com.github.burravlev.network;

import lombok.Getter;

@Getter
class AddressParser {
    private final String host;
    private final int port;
    private final String id;

    AddressParser(String address) {
        String[] parts = address.split("/");
        if (parts.length < 3) throw new IllegalStateException("cannot parse address: " + address);
        this.host = parts[0];
        this.port = Integer.parseInt(parts[1]);
        this.id = parts[2];
    }
}
