package com.centro.platform.api.java.security;

import com.centro.platform.api.java.security.business.CentroCertificateFactory;

import java.security.cert.Certificate;
import java.util.Date;

public class CentroLogin {
	
	private static final long expiryTimeInMillis = 1000 * 60 * 60 * 24 * 7;

	public static String loginUser(String username, String password) throws Exception {
		
		LoginInfo loginInfo = new LoginInfo(username, password);
		Date expiryDate = new Date(System.currentTimeMillis() + expiryTimeInMillis);
		Certificate certificate = CentroCertificateFactory.generateCertificate(loginInfo, expiryDate);
		return certificate.toString();
		
	}
	
}
