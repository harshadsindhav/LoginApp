package com.centro.platform.api.java.security.business;


import java.util.Date;

public class EncodedDateValue extends EncodedValue {

    private static final int kBytesToHoldTime=8;

    public EncodedDateValue(Date object) {
        super(object, kDate);
    }

    protected EncodedDateValue() {
        super(kDate);
    }

    /**
     * Convert the wrapped object into bytes that
     * getObjectFromBytes can later restore object from.
     *
     */
    protected byte[] getObjectBytes() {
        Date date = (Date) getObject();
        return convertToFixedLengthField(date.getTime(),kBytesToHoldTime);
    }

    /**
     * Convert bytes returned by getObjectBytes() back
     * into original object
     */
    protected Object getObjectFromBytes(byte[] objectBytes) {
        long time =  convertToNumber(objectBytes);
        return new Date(time);
    }
}
