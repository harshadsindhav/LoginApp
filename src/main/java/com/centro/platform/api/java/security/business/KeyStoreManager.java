package com.centro.platform.api.java.security.business;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyStoreManager {
	
	private static PrivateKey privateKey = null;
	private static PublicKey publicKey = null;
	
	private static final String kPrivateKeyIdentifier = "publicKey";
	private static final String kPublicKeyIdentifier = "privateKey";
	
	public static PublicKey getPublicKey() {
		if(privateKey == null || publicKey == null) {
			populateKeys();
		} 
		return publicKey;
	}
	
	public static PrivateKey getPrivateKey() {
		if(privateKey == null || publicKey == null) {
			populateKeys();
		}
		return privateKey;
	}
	
	private static synchronized void populateKeys() {
		File publicKeyFile = null;
		File privateKeyFile = null;
		
		publicKeyFile = new File("publicKey.der");
		privateKeyFile = new File("privateKey.der");
		
		try {
			
			KeyFactory keyFactory = KeyFactory.getInstance("DSA");
			readFromFile(privateKeyFile, publicKeyFile, keyFactory);
			
			if(privateKey == null || publicKey == null) {
				KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
				KeyPair keyPair = keyPairGenerator.generateKeyPair();
				privateKey = keyPair.getPrivate();
				publicKey = keyPair.getPublic();
				
				//Save generated Keys
				saveKeysToFile(keyFactory, publicKeyFile, privateKeyFile);
				
			}
		}catch(Exception e) {
			
		}
	}
	
	private static void saveKeysToFile(KeyFactory keyFactory, File publicKeyFile, File privateKeyFile) throws Exception {
		EncodedKeySpec spec = (EncodedKeySpec) keyFactory.getKeySpec(publicKey, X509EncodedKeySpec.class);
		writeToBinaryFile(publicKeyFile, spec.getEncoded());
		
		spec = (EncodedKeySpec) keyFactory.getKeySpec(privateKey, PKCS8EncodedKeySpec.class);
		writeToBinaryFile(privateKeyFile, spec.getEncoded());
	}
	
	private static void writeToBinaryFile(File file, byte[] data) throws IOException {
		OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
		try {
			os.write(data);
		} finally {
			os.close();
		}
	}
	private static byte[] readBinaryFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
		try {
			int maxSize = 1024 * 8;
			byte[] buffer = new byte[maxSize];
			int byteRead = is.read(buffer, 0, maxSize);
			while(byteRead != -1) {
				byteOS.write(buffer, 0, byteRead);
				
				byteRead = is.read(buffer, 0, maxSize);
			}
		}finally {
			is.close();
			byteOS.flush();
		}
		return byteOS.toByteArray();
	}
	
	private static void readFromFile(File privateKeyFile, File publicKeyFile, KeyFactory keyFactory) throws Exception {
		try {
			byte[] encodedKey = readBinaryFile(publicKeyFile);
			X509EncodedKeySpec encodedPublicKey = new X509EncodedKeySpec(encodedKey);
			publicKey = keyFactory.generatePublic(encodedPublicKey);
			
			encodedKey = readBinaryFile(privateKeyFile);
			PKCS8EncodedKeySpec encodedPrivateKey = new PKCS8EncodedKeySpec(encodedKey);
			privateKey = keyFactory.generatePrivate(encodedPrivateKey);
			
		}catch(Exception e) {
			privateKey = null;
		}
	}
}