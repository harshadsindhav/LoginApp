package com.centro.platform.api.java.security.hashing;

import com.centro.platform.api.java.security.hashing.helper.CentroPasswordHashHelper;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CentroPasswordHash {

    private static SecureRandom random = null;


    public static String generateHash(String password) throws Exception {
        CentroPasswordHashHelper helper = new CentroPasswordHashHelper();
        String hash = helper.generateHash(password);
        return hash;
    }

    public static String generatePasswordSalt() {
        if(random == null) {
            try {
                random = SecureRandom.getInstance("SHA1PRNG");
            } catch(NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            random.nextBytes(new byte[20]);
        }
        return String.valueOf(random.nextLong());
    }

}
