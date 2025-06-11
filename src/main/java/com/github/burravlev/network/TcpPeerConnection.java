package com.github.burravlev.network;

import com.github.burravlev.util.JsonSerializer;
import com.github.burravlev.util.RsaUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
class TcpPeerConnection implements PeerConnection {
    private static final long TIMEOUT_SEC = 10;

    private final Socket socket;
    private final NodeIdentity innerIdentity;
    private final PublicIdentity peerIdentity;
    private final PrintWriter writer;
    private final BufferedReader reader;
    private final AtomicBoolean running = new AtomicBoolean(true);

    @SneakyThrows
    TcpPeerConnection(Socket client, NodeIdentity identity) {
        this.socket = client;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.innerIdentity = identity;
        this.peerIdentity = CompletableFuture.supplyAsync(new HandshakeSupplier(reader, identity))
            .get(TIMEOUT_SEC, TimeUnit.SECONDS);
        logConnected(peerIdentity);
    }

    @SneakyThrows
    TcpPeerConnection(String address, NodeIdentity identity) {
        var parsed = new AddressParser(address);
        this.socket = new Socket(parsed.getHost(), parsed.getPort());
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.innerIdentity = identity;
        this.peerIdentity = new PublicIdentity()
            .setHost(parsed.getHost())
            .setPort(parsed.getPort())
            .setId(parsed.getId());
        sendHandshake();
        logConnected(peerIdentity);
    }

    private void logConnected(Identity identity) {
        log.info("new connection: {}:{} to {}", identity.getHost(), identity.getPort(), innerIdentity.getPort());
    }

    private void sendHandshake() {
        var event = new Message()
            .setData(JsonSerializer.serializeTree(new PublicIdentity()
                .setHost(innerIdentity.getHost())
                .setPort(innerIdentity.getPort())
                .setId(innerIdentity.getId())
            ));
        event.getHeaders().put(Headers.EVENT_TYPE, EventType.HANDSHAKE.code());
        send(event);
    }

    @Override
    @SneakyThrows
    public Message read() {
        String encrypted = reader.readLine();
        if (encrypted == null) return null;
        String json = CryptoUtil.decrypt(encrypted, innerIdentity.getPrivateKey());
        return JsonSerializer.deserialize(json, Message.class);
    }

    @Override
    @SneakyThrows
    public Message read(int timeout) {
        return CompletableFuture.supplyAsync(this::read)
            .get(timeout, TimeUnit.SECONDS);
    }

    @Override
    @SneakyThrows
    public void send(Message data) {
        String json = JsonSerializer.serialize(data);
        String encrypted = CryptoUtil.encrypt(json, RsaUtil.publicFromBase64(peerIdentity.getId()));
        writer.println(encrypted);
    }

    @Override
    public String getAddress() {
        return peerIdentity.getAddress();
    }

    @Override
    public void close() throws Exception {
        running.set(false);
        writer.close();
        reader.close();
        socket.close();
        log.info("connection closed: {}", innerIdentity.getPort());
    }
}
