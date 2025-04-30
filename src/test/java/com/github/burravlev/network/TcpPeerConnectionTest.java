package com.github.burravlev.network;

import com.github.burravlev.util.JsonSerializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ServerSocket;

@Slf4j
public class TcpPeerConnectionTest {

    @Test
    public void testClientConnection() throws Exception {
        int port = 8080;
        Message message = new Message()
            .setData(JsonSerializer.serializeTree("Hello world!"));
        ServerSocket server = new ServerSocket(port);
        NodeIdentity identity = new NodeIdentity(port);
        NetworkExecutor.execute(() -> {
            try {
                PeerConnection connection = new TcpPeerConnection(server.accept(), identity);
                Message received = connection.read();
                log.info("{}", received);
                Assertions.assertEquals(message, received);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        var connection = new TcpPeerConnection("localhost/8080/" + identity.getId(), new NodeIdentity(9090));
        connection.send(message);
        connection.close();
    }
}
