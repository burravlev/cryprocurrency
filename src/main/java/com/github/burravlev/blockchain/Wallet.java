package com.github.burravlev.blockchain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.burravlev.util.RsaUtil;
import lombok.*;
import lombok.experimental.Accessors;

import java.security.KeyPair;

@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@EqualsAndHashCode
public class Wallet {
    private String address;
    @JsonIgnore
    private String privateKey;

    @SneakyThrows
    public Wallet() {
        KeyPair keyPair = RsaUtil.getNewKeyPair();
        this.address = RsaUtil.keyToBase64Encoded(keyPair.getPublic());
        this.privateKey = RsaUtil.keyToBase64Encoded(keyPair.getPrivate());
    }

    @SneakyThrows
    public Wallet(String privateKey) {
        this.privateKey = privateKey;
        this.address = RsaUtil.getPublic(privateKey);
    }
}
