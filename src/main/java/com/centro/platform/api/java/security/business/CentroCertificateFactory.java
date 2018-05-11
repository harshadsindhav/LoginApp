package com.centro.platform.api.java.security.business;

import com.centro.platform.api.java.security.LoginInfo;

import java.security.cert.Certificate;
import java.util.Date;

public class CentroCertificateFactory {
	
	/**
	 * Returns respective password from given centro certificate
	 * @param certificate
	 * @return
	 * @throws Exception
	 */
	public static String convertToPassword(Certificate certificate) throws Exception {
		
		return null;
	}
	
	public static String convertToPassword(String certificate) throws Exception {
		return null;
	}
	
	public static Certificate convertFromPassword(String password) throws Exception {
		return null;
	}

	/**
	 * Returns CentroCertificate for given login information
	 * @param loginInfo
	 * @param expiredOn
	 * @return CentroCertificate
	 * @throws Exception
	 */
	public static Certificate generateCertificate(LoginInfo loginInfo, Date expiredOn) throws Exception {
		return new CentroCertificate(loginInfo, expiredOn);
	}
}
