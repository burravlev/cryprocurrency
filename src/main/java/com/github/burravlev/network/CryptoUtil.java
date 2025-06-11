package com.github.burravlev.network;

import com.github.burravlev.util.AesUtil;
import com.github.burravlev.util.Base64Util;
import com.github.burravlev.util.JsonSerializer;
import com.github.burravlev.util.RsaUtil;
import lombok.experimental.UtilityClass;

import javax.crypto.SecretKey;
import java.security.Key;

@UtilityClass
class CryptoUtil {
    String encrypt(String data, Key publicKey) {
        SecretKey key = AesUtil.getSecret();
        String encryptedData = AesUtil.encrypt(data, key);
        String encryptedKey = RsaUtil.encrypt(
            AesUtil.toBase64(key), publicKey
        );
        var pack = new PeerPackage()
            .setData(encryptedData)
            .setKey(encryptedKey);
        return Base64Util.encodeToString(JsonSerializer.serialize(pack));
    }

    String decrypt(String pack, Key privateKey) {
        PeerPackage peerPackage = JsonSerializer.deserialize(
            Base64Util.decodeToString(pack), PeerPackage.class
        );
        String decryptedKey = RsaUtil.decrypt(peerPackage.getKey(), privateKey);
        SecretKey key = AesUtil.fromBase64(decryptedKey);
        return AesUtil.decrypt(peerPackage.getData(), key);
    }
}
