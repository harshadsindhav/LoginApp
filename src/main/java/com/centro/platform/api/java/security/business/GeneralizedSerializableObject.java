package com.centro.platform.api.java.security.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class GeneralizedSerializableObject implements Serializable {

	private Map attributes = new TreeMap<>();
	
	
    public void setAttribute(String attrName, Object object) {
        if (object == null) 
        	attributes.remove(attrName);
        else 
        	attributes.put(attrName, object);
    }
    
    public Object getAttribute(String attrName) {
        return attributes.get(attrName);
    }

       public byte[] getBytes() {
        if (attributes.size()==0) return new byte[0];

        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
        try {
            Iterator i = attributes.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry entry = (Map.Entry) i.next();
                EncodedValue name = EncodedValue.getInstance(entry.getKey());
                EncodedValue value = EncodedValue.getInstance(entry.getValue());
                byteOS.write(name.getBytes());
                byteOS.write(value.getBytes());
            }
            byteOS.flush();
            return byteOS.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static GeneralizedSerializableObject getInstance(byte[] bytes) {
        GeneralizedSerializableObject result = new GeneralizedSerializableObject();

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            EncodedValue name = EncodedValue.getInstanceFromStream(in);
            EncodedValue value = EncodedValue.getInstanceFromStream(in);
            while (name != null) {
                result.setAttribute((String) name.getObject(),value.getObject());
                name = EncodedValue.getInstanceFromStream(in);
                value = EncodedValue.getInstanceFromStream(in);
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
