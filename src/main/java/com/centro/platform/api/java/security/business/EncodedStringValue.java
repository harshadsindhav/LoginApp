package com.centro.platform.api.java.security.business;


import java.io.*;

public class EncodedStringValue extends EncodedValue {

    public EncodedStringValue(String object) {
        super(object, kString);
    }

    protected EncodedStringValue() {
        super(kString);
    }

    protected byte[] getObjectBytes() {
        try {
            return ((String) getObject()).getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected Object getObjectFromBytes(byte[] objectBytes) {
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader( new ByteArrayInputStream(objectBytes), "UTF8" ));
            String str = reader.readLine();
            while (reader.ready()) {
                str += "\n"+reader.readLine();
            }
            reader.close();
            return str;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
