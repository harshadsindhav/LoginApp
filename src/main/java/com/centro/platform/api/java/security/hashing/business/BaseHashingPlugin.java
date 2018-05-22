package com.centro.platform.api.java.security.hashing.business;

import com.centro.platform.api.java.security.business.EncryptionUtil;

import java.security.MessageDigest;

public abstract class BaseHashingPlugin {

    public String generateHash(String password) throws Exception {

        // 1. Get algorithm name by invoking getAlgorithmName()
        // 2. Instantiate MessageDigest with the hashing algorithm name
        // 3. Generate hash value (byte array)
        // 4. Convert byte array into base hex encoding format. The password will be stored in DB in this format.

        try {
            String hasingAlgorithmName = getAlgorithmName();
            MessageDigest messageDigest = MessageDigest.getInstance(hasingAlgorithmName);

            try {
                messageDigest.update(password.getBytes("UTF-8"));

            }catch(Exception e) {
                messageDigest.update(password.getBytes());
            }

            byte[] digest = messageDigest.digest();
            String passwordHash = EncryptionUtil.base16encode(digest);

            return passwordHash;

        }catch(Exception e) {
            throw new Exception(e.toString());
        }

    }

    public abstract String getAlgorithmName() throws Exception;

}
