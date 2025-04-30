package com.github.burravlev.network;

import lombok.Getter;
import lombok.SneakyThrows;

import java.net.DatagramSocket;
import java.net.InetAddress;

@Getter
class IpV4Resolver {
    private final String host;

    @SneakyThrows
    IpV4Resolver() {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            this.host = socket.getLocalAddress().getHostAddress();
        }
    }
}
