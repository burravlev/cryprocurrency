package com.github.burravlev.node;

import com.github.burravlev.storage.RowMapper;
import com.github.burravlev.storage.Storage;
import com.github.burravlev.storage.StorageFactory;
import com.github.burravlev.util.ResourceUtil;
import com.github.burravlev.util.RsaUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.util.List;
import java.util.UUID;

class NodeInstanceTest {
    @Test
    @SneakyThrows
    void shouldRunNodes() {
        String db1 = ResourceUtil.getAbsolutePathAsString(UUID.randomUUID() + ".db");
        String db2 = ResourceUtil.getAbsolutePathAsString(UUID.randomUUID() + ".db");
        ResourceUtil.deleteOnExit(db1);
        ResourceUtil.deleteOnExit(db2);
        KeyPair pair1 = RsaUtil.generateKeyPair();
        KeyPair pair2 = RsaUtil.generateKeyPair();
        NodeInstance.run(new NodeConfig()
            .setPort(8080)
            .setKey(RsaUtil.keyToBase64Encoded(pair1.getPrivate()))
            .setStoragePath(db1)
        );
        NodeInstance.run(
            new NodeConfig()
                .setPort(9090)
                .setStoragePath(db2)
                .setKey(RsaUtil.keyToBase64Encoded(pair2.getPrivate()))
                .setAddresses(List.of(
                    "192.168.0.140/8080/" + RsaUtil.keyToBase64Encoded(pair1.getPublic()))
                )
        );
        Storage storage1 = StorageFactory.byFile(db1);
        Storage storage2 = StorageFactory.byFile(db2);
        Assertions.assertEquals(0,
            storage1.executeQuery("SELECT count(*) FROM blockchain_tmp", getCount()));
        Assertions.assertEquals(0,
            storage2.executeQuery("SELECT count(*) FROM blockchain_tmp", getCount()));
        Assertions.assertEquals(1,
            storage1.executeQuery("SELECT count(*) FROM blockchain", getCount()));
        Assertions.assertEquals(1,
            storage2.executeQuery("SELECT count(*) FROM blockchain", getCount()));
        Assertions.assertEquals(0,
            storage1.executeQuery("SELECT count(*) FROM tx_tmp", getCount()));
        Assertions.assertEquals(0,
            storage2.executeQuery("SELECT count(*) FROM tx_tmp", getCount()));
        Assertions.assertEquals(1,
            storage1.executeQuery("SELECT count(*) FROM tx", getCount()));
        Assertions.assertEquals(1,
            storage2.executeQuery("SELECT count(*) FROM tx", getCount()));
        storage1.close();
        storage2.close();
    }

    private static RowMapper<Integer> getCount() {
        return rs -> {
            try {
                return rs.getInt(1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
