package com.github.burravlev.network;

import com.github.burravlev.util.RsaUtil;

import java.util.List;

public class NodeBuilder {
    private int port = PortGenerator.generate();
    private String privateKey;
    private List<String> addresses;

    public NodeBuilder() {
    }

    public NodeBuilder port(int port) {
        this.port = port;
        return this;
    }

    public NodeBuilder addresses(List<String> addresses) {
        this.addresses = addresses;
        return this;
    }

    public NodeBuilder privateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public Node build() {
        NodeIdentity identity = getIdentity();
        var node = new TcpNode(port, identity);
        if (addresses != null && !addresses.isEmpty()) {
            node.connect(addresses);
        }
        return node;
    }

    private NodeIdentity getIdentity() {
        NodeIdentity identity;
        if (privateKey != null && !privateKey.isEmpty()) {
            identity = new NodeIdentity(RsaUtil.privateFromBase64Encoded(privateKey), port);
        } else {
            identity = new NodeIdentity(port);
        }
        return identity;
    }
}
