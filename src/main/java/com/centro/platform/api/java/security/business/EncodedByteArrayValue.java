package com.centro.platform.api.java.security.business;


public class EncodedByteArrayValue extends EncodedValue {

    public EncodedByteArrayValue(Object object) {
        super(object, kByteArray);
    }

    protected EncodedByteArrayValue() {
        super(kByteArray);
    }

    protected byte[] getObjectBytes() {
        return (byte[]) getObject();
    }

    protected Object getObjectFromBytes(byte[] objectBytes) {
        return objectBytes;
    }
}
