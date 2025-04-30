package com.github.burravlev.blockchain;

import com.github.burravlev.storage.RowMapper;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TransactionMapper implements RowMapper<List<Transaction>> {
    @Override
    public List<Transaction> map(ResultSet rs) {
        var tx = new ArrayList<Transaction>();
        try {
            while (rs.next()) {
                var curr = new Transaction()
                    .setId(rs.getString("id"))
                    .setRandBytes(rs.getString("rand_bytes"))
                    .setPrevBlockHash(rs.getString("prev_block_hash"))
                    .setSender(rs.getString("sender"))
                    .setReceiver(rs.getString("receiver"))
                    .setAmount(rs.getString("amount"))
                    .setTimestamp(rs.getLong("timestamp"))
                    .setCurrBlockHash(rs.getString("curr_block_hash"))
                    .setSignature(rs.getString("signature"));
                tx.add(curr);
            }
        } catch (Exception e) {
            log.error("cannot process query: ", e);
            throw new RuntimeException(e);
        }
        return tx;
    }
}
