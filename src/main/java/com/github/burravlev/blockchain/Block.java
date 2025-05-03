package com.github.burravlev.blockchain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.burravlev.util.RsaUtil;
import com.github.burravlev.util.Sha256Util;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@EqualsAndHashCode
public class Block {
    private int nonce;
    private int difficulty;
    private String prevHash;
    private String miner;
    private String hash;
    private long timestamp = System.currentTimeMillis();
    private Collection<Transaction> transactions = Collections.emptyList();
    private String signature;

    public Block(String prevHash,
                 Wallet miner,
                 int difficulty,
                 Collection<Transaction> transactions) {
        this.miner = miner.getAddress();
        this.difficulty = difficulty;
        this.transactions = transactions;
        this.prevHash = prevHash;
        mine();
        sign(miner.getPrivateKey());
    }

    private void mine() {
        this.hash = hash();
        while (!isHashValid()) {
            timestamp = System.currentTimeMillis();
            nonce++;
            this.hash = hash();
        }
    }

    private boolean isHashValid() {
        for (int i = 0; i < difficulty; i++) {
            if (hash.charAt(i) != '0') return false;
        }
        return true;
    }

    public String hash() {
        log.info("block data: {}", dataForHash());
        return Sha256Util.hash(dataForHash());
    }

    @SneakyThrows
    private void sign(String privateKey) {
        this.signature = RsaUtil.sign(dataForSign(), RsaUtil.privateFromBase64Encoded(privateKey));
    }

    private String dataForHash() {
        return String.valueOf(nonce) +
            difficulty +
            prevHash +
            miner +
            timestamp +
            transactions;
    }

    private String dataForSign() {
        return dataForHash() + hash;
    }

    public boolean isValid() {
        boolean isHashValid = isHashValid() && Objects.equals(this.hash, hash());
        boolean isSignValid = RsaUtil.verifySign(dataForSign(), signature, miner);
        return isHashValid && isSignValid;
    }
}
