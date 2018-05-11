package com.centro.platform.api.java.security.business;


import java.security.*;
import java.security.spec.*;

public class EncodedPublicKeyValue extends EncodedValue {
    public EncodedPublicKeyValue(PublicKey object) {
        super(object, kPublicKey);
    }

    protected EncodedPublicKeyValue() {
        super(kPublicKey);
    }

    /**
     * Convert the wrapped object into bytes that
     * getObjectFromBytes can later restore object from.
     *
     */
    protected byte[] getObjectBytes() {
        EncodedKeySpec spec = null;
        try {
            spec = (EncodedKeySpec) getKeyFactory().getKeySpec((PublicKey)getObject() ,X509EncodedKeySpec.class);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return spec.getEncoded();
    }

    /**
     * Convert bytes returned by getObjectBytes() back
     * into original object
     */
    protected Object getObjectFromBytes(byte[] objectBytes) {
        X509EncodedKeySpec encodedPublic = null;
        try {
            encodedPublic = new X509EncodedKeySpec(objectBytes);
            return getKeyFactory().generatePublic(encodedPublic);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private KeyFactory getKeyFactory() {
        try {
            return KeyFactory.getInstance("DSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
