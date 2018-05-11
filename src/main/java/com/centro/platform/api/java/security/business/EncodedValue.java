package com.centro.platform.api.java.security.business;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.*;

/**
 * responsible for streaming itself into bytes
 * and instantiating itself from bytes.  Needed for
 * jre version independent certificate encoding.
 */
public abstract class EncodedValue {

    private static final int kBytesInSizeField = 4;
    private static final byte kZero=0;
    private static final byte kFF= (byte)(long)255;  //ff

    public static final byte kString=1;
    public static final byte kByteArray=2;
    public static final byte kDate=3;
    public static final byte kPublicKey=4;

    public static final long kNegOffset = 255 + (-1 * (long) kFF);

    private Object object;
    private byte type;

    //***************** constructor *****************

    protected EncodedValue(Object object, byte type) {
        this.object = object;
        this.type = type;
    }

    protected EncodedValue(byte type) {
        this.type = type;
    }


    public static EncodedValue getInstance(Object object) {
        switch (getType(object)) {
            case kString : return new EncodedStringValue((String) object);
            case kByteArray : return new EncodedByteArrayValue((byte[]) object);
            case kDate : return new EncodedDateValue((Date) object);
            case kPublicKey : return new EncodedPublicKeyValue((PublicKey) object);
            default: throw new IllegalArgumentException("Unsupported type : "+ object.getClass().getName());
        }
    }

    private static byte getType(Object object) {
        if (object instanceof String) return kString;
        if (object instanceof byte[]) return kByteArray;
        if (object instanceof Date) return kDate;
        if (object instanceof PublicKey) return kPublicKey;
        throw new IllegalArgumentException("Unsupported type : "+ object.getClass().getName());
    }

    //**************** accessors **********************
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    /************** byte conversion methods ***********/
    /**
     * Format is:
     * byte 1 = object type
     * byte kBytesInSizeField = size of object
     * byte (size of object) = object represented as bytes
     */
    public byte[] getBytes() {
        byte[] objectBytes = getObjectBytes();
        byte[] sizeField = convertToFixedLengthField(objectBytes.length, kBytesInSizeField);

        byte[] result = new byte[1 + kBytesInSizeField + objectBytes.length];
        int index = 0;
        result[index++]=type;
        System.arraycopy(sizeField,0,result,index, sizeField.length);
        index += sizeField.length;
        System.arraycopy(objectBytes,0,result,index, objectBytes.length);
        return result;

    }

    /**
     * Convert the wrapped object into bytes that
     * getObjectFromBytes can later restore object from.
     *
     */
	protected abstract  byte[] getObjectBytes();
    /**
     * Convert bytes returned by getObjectBytes() back
     * into original object
     */
	protected abstract Object getObjectFromBytes(byte[] objectBytes);



    /**
     * returns null if at end of stream, or else reads EncodedValue from stream
     */
    public static EncodedValue getInstanceFromStream(InputStream inputStream) throws java.io.IOException {
        byte[] typeField = new byte[1];


        int bytesRead = inputStream.read(typeField,0,1);
        byte type = typeField[0];
        if (bytesRead < 1) return null;

        byte[] sizeField = new byte[kBytesInSizeField];
        bytesRead = inputStream.read(sizeField,0,kBytesInSizeField);
        int size = (int) convertToNumber(sizeField);
        if (bytesRead < kBytesInSizeField) throw new IllegalArgumentException("Unable to read in complete size field.");

        byte[] objectBytes = new byte[size];
        bytesRead = inputStream.read(objectBytes,0,size);
        if (bytesRead < size) throw new IllegalArgumentException("Unable to read in complete object. Object was "+size+
                                                                " bytes, but only read "+bytesRead);

        EncodedValue result = null;
        switch ( typeField[0] ) {
            case kString : {
                result = new EncodedStringValue();
                break;
            }
            case kByteArray: {
                result = new EncodedByteArrayValue();
                break;
            }
            case kDate: {
                result = new EncodedDateValue();
                break;
            }

           case kPublicKey: {
                result = new EncodedPublicKeyValue();
                break;
            }
                default:
                 throw new IllegalArgumentException("Unkown data type: "+type);
        }
        Object value = result.getObjectFromBytes(objectBytes);
        //should never happen, but check anyway
        if (getType(value) != result.getType() ) throw new IllegalStateException("EncodedValue type mismatch");
        result.setObject(value);
        return result;
    }


    /**
     * Need to support jdk 1.3 so cannot use ByteBuffer
     */
    protected static byte[] convertToFixedLengthField(long numberValue, int numberOfBytes ) {
        byte[] field = new byte[numberOfBytes];

        if (numberValue < 0) throw new IllegalArgumentException("Cannot handle negative value: "+ numberValue);

        Arrays.fill(field,kZero);
        int index = numberOfBytes-1;
        long value = numberValue;
        while (value != 0) {
            long nextValue = value/256;
            long remainder = value - nextValue*256;
            field[index--]=new Long(remainder).byteValue();
            value = nextValue;
            if (index < 0 && value != 0) throw new IllegalArgumentException("Cannot represent "+value+" in "+numberOfBytes+" bytes.");
        }
        return field;
    }

    protected static long convertToNumber(byte[] field) {
        long value = 0;
        long multiplier = 1;
        for (int i=field.length -1 ;i >= 0 ; i--) {
            long digit = (long) field[i];
            if (digit < 0) digit += kNegOffset;
            value += digit * multiplier;
            multiplier = multiplier * 256;
        }
        return value;
    }
}
