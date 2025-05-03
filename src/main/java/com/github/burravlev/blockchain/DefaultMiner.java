package com.github.burravlev.blockchain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
class DefaultMiner implements Miner {
    private final TxStorage txStorage;
    private final ChainStorage chainStorage;
    private final DifficultyCalculator calculator;
    private final EventPublisher publisher;
    private final Wallet wallet;
    private final BlockMiner blockMiner;

    public void mine() {
        var thread = new Thread(() -> {
            try {
                var transactions = txStorage.findNotAccepted();
                String lastHash = chainStorage.getLastHash();
                var block = blockMiner.mine(
                    lastHash, wallet, calculator.calc(), transactions
                );
                block.setTransactions(transactions);
                txStorage.markAccepted(transactions.stream().map(Transaction::getId)
                        .collect(Collectors.toList()),
                    block.getHash()
                );
                lastHash = chainStorage.getLastHash();
                System.out.println(lastHash);
                if (lastHash.equals(block.getPrevHash())) {
                    chainStorage.save(block);
                    publisher.newBlock(block);
                }
            } catch (Exception e) {
                log.error("", e);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
