package com.github.burravlev.network;

interface PeerConnection extends AutoCloseable {
    Message read();

    Message read(int timeout);

    void send(Message data);

    String getAddress();
}
