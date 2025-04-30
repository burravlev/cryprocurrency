package com.github.burravlev.network;

import com.github.burravlev.util.JsonSerializer;
import com.github.burravlev.util.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

@Slf4j
public class CryptoUtilTest {
    @Test
    public void shouldEncryptToStringAndDecrypt() {
        KeyPair pair = RsaUtil.generateKeyPair();
        String data = getData();
        log.info("data: {}", data);
        String pack = CryptoUtil.encrypt(data, pair.getPublic());
        String decrypted = CryptoUtil.decrypt(pack, pair.getPrivate());
        Assertions.assertEquals(data, decrypted);
    }

    private static String getData() {
        var pair = RsaUtil.generateKeyPair();
        Message event = new Message()
            .setEvent(EventType.HANDSHAKE.code())
            .setData(JsonSerializer.serializeTree(new PublicIdentity()
                .setId(RsaUtil.publicToBase64(pair.getPublic()))
                .setHost("localhost")
                .setPort(8080)
            ));
        return JsonSerializer.serialize(event);
    }
}
