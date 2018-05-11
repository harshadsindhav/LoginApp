package com.centro.platform.api.java.security.business;


import com.centro.platform.api.java.security.LoginInfo;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Date;

public class CentroCertificate extends Certificate implements Serializable {

	public static final String kCentroCertificateType = "Saba";
	private CertificateData certificateData;
	
	private CentroCertificate(CertificateData certData) {
		super(certData.getCertificateType());
		this.certificateData = certData;
	}
	
	public CentroCertificate(LoginInfo loginInfo, Date expiresOn) {
		this(loginInfo, null, expiresOn);
	}
	
	public CentroCertificate(LoginInfo loginInfo, String username, Date expiresOn) {
		super(kCentroCertificateType);
		if(expiresOn == null) {
			throw new IllegalArgumentException("Must pass non-null expiresOn");
		}
		if(expiresOn.before(new Date())) {
			throw new IllegalArgumentException("Must pass expiresOn date that is after now.");
		}
		if(username == null || (username.equalsIgnoreCase(loginInfo.getUsername()))) {
			username = loginInfo.getUsername();
		}
		try {
			PublicKey publicKey = KeyStoreManager.getPublicKey();
			certificateData = new CertificateData(CertificateData.kCentroCertificateType, expiresOn, username, publicKey, null);
			certificateData.setSignature(KeyStoreManager.getPrivateKey());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected static Certificate generateCertificate(String certificateAsString) throws Exception {
		byte[] bytes;
		try {
			bytes = EncryptionUtil.base16decode(certificateAsString);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		return generateCertificate(bytes);
	}
	
	private static Certificate generateCertificate(byte[] bytes) throws Exception {
		CertificateData data = null;
		try {
			data = CertificateData.getFromBytes(bytes);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		return new CentroCertificate(data);
	}

	@Override
	public byte[] getEncoded() throws CertificateEncodingException {
		return certificateData.getBytes();
	}

	@Override
	public void verify(PublicKey key, String sigProvider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
			NoSuchProviderException, SignatureException {
		certificateData.verify(key);
		
	}
	
	
	@Override
	public String toString() {
		try {
			return EncryptionUtil.base16encode(getEncoded());
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public PublicKey getPublicKey() {
		return certificateData.getPublicKey();
	}
}
