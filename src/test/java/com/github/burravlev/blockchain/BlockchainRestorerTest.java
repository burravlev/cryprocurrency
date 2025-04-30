package com.github.burravlev.blockchain;

import com.github.burravlev.storage.Storage;
import org.junit.jupiter.api.Test;

public class BlockchainRestorerTest extends AbstractChainTest {
    @Test
    public void shouldRestoreBlock() throws Exception {
        Storage storage = testStorage();

        storage.close();
    }
}
