package com.github.burravlev.network;

import com.github.burravlev.util.JsonSerializer;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class HandshakeSupplier implements Supplier<PublicIdentity> {
    private final BufferedReader reader;
    private final NodeIdentity identity;

    @Override
    @SneakyThrows
    public PublicIdentity get() {
        String encrypted = reader.readLine();
        String decrypted = CryptoUtil.decrypt(encrypted, identity.getPrivateKey());
        Message message = JsonSerializer.deserialize(decrypted, Message.class);
        if ((int) message.getHeaders().get(Headers.EVENT_TYPE) != EventType.HANDSHAKE.code()) {
            throw new IllegalStateException("cannot handshake, invalid message: " +  decrypted);
        }
        return JsonSerializer.fromTree(message.getData(), PublicIdentity.class);
    }
}
