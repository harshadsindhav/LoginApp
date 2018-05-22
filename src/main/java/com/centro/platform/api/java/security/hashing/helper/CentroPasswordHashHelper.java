package com.centro.platform.api.java.security.hashing.helper;

import com.centro.platform.api.java.security.hashing.business.BaseHashingPlugin;

public class CentroPasswordHashHelper {

    private static BaseHashingPlugin mHashingPlugin = null;

    public static final String kHashingPluginDefaultValue = "com.centro.platform.api.java.security.hashing.business.CentroSHA256HashingPlugin";

    static {

        String pluginClassName = kHashingPluginDefaultValue;
        if(pluginClassName != null) {
            Class pluginClass = null;

            try {
                pluginClass = Class.forName(pluginClassName);
            }catch(Exception e) {
                try {
                    pluginClass = Thread.currentThread().getContextClassLoader().loadClass(pluginClassName);
                } catch(Exception e2) {
                    System.out.println(e.toString());
                }
            }
            if(pluginClass != null) {
                try {
                    mHashingPlugin = (BaseHashingPlugin) pluginClass.newInstance();
                }catch(Exception e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    public CentroPasswordHashHelper() {

    }

    public String generateHash(String password) throws Exception {
        return mHashingPlugin.generateHash(password);
    }
}
