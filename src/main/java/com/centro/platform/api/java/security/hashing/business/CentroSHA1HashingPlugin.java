package com.centro.platform.api.java.security.hashing.business;

public class CentroSHA1HashingPlugin extends BaseHashingPlugin {

    @Override
    public String getAlgorithmName() throws Exception {
        return "SHA-1";
    }
}
