package com.github.burravlev.blockchain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.burravlev.util.Base64Util;
import com.github.burravlev.util.RsaUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {
    private String id = UUID.randomUUID().toString();
    private String randBytes = Base64Util.encodeToString(RandomBytesUtil.get());
    private String prevBlockHash;
    private String sender;
    private String receiver;
    private String amount;
    private long timestamp = ZonedDateTime.now().toEpochSecond();
    private String signature;
    private String currBlockHash;

    public Transaction(String prevBlockHash, Wallet from, String to, String amount) {
        this.sender = from.getAddress();
        this.receiver = to;
        this.amount = amount;
        this.prevBlockHash = prevBlockHash;
        this.signature = RsaUtil.sign(
            getDataForSigning(), from.getPrivateKey()
        );
    }

    private String getDataForSigning() {
        return id + randBytes + prevBlockHash + sender + receiver + amount + timestamp;
    }

    @Override
    public String toString() {
        return getDataForSigning() + signature;
    }
}
