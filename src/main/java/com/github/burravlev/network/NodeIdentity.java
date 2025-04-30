package com.github.burravlev.network;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.burravlev.util.JsonSerializer;
import com.github.burravlev.util.RsaUtil;
import lombok.Getter;

import java.security.Key;
import java.security.KeyPair;
import java.security.PrivateKey;

@Getter
class NodeIdentity implements Identity {
    private final String host;
    private final int port;
    @JsonIgnore
    private final Key publicKey;
    @JsonIgnore
    private final Key privateKey;

    NodeIdentity(int port) {
        this.host = new IpV4Resolver().getHost();
        this.port = port;
        KeyPair pair = RsaUtil.generateKeyPair();
        publicKey = pair.getPublic();
        privateKey = pair.getPrivate();
    }

    NodeIdentity(PrivateKey privateKey, int port) {
        this.host = new IpV4Resolver().getHost();
        this.port = port;
        this.privateKey = privateKey;
        this.publicKey = RsaUtil.getPublic(this.privateKey);
    }

    public String getId() {
        return RsaUtil.publicToBase64(publicKey);
    }

    @Override
    public String toString() {
        return JsonSerializer.serialize(this);
    }
}
