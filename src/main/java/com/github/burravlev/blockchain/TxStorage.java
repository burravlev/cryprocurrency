package com.github.burravlev.blockchain;

import java.util.Collection;
import java.util.List;

interface TxStorage {

    void save(Transaction tx);

    List<Transaction> findNotAccepted();

    void markAccepted(Collection<String> ids, String blockHash);

    List<Transaction> getTransactions(String address);

    List<Transaction> getByHash(String blockHash);
}
