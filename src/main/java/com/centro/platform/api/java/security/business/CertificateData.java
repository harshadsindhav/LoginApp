package com.centro.platform.api.java.security.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.Date;

public class CertificateData implements Serializable {

	public static final String kCentroCertificateType = "centro";
	public static String DATA_SEPERATOR = "^#^";
    public static String DATA_SEPERATOR_REGEX = "\\^#\\^";
	private static byte[] BYTES_SEPERATOR = new byte[] {'$', 'A', 'B', '@'};
	
	
	private String certificateType;
	private Date expiresOn;
	private String username;
	private byte[] signature;
	private PublicKey publicKey;
	
	public static final String kExpiresAttrName = "expire";
	private static final String kUsernameAttrName = "user";
	public static final String kSiteAttrName = "site";
	private static final String kLocaleAttrName = "locale";
	private static final String kSignatureAttrName = "sig";
	private static final String kPublicKeyAttrName = "key";
	protected static final String kCertificateTypeAttrName = "type";
    public static final String kCertMaxUsageCountAttrName = "certMaxUsageCount";
	
	public CertificateData() {
		
	}
	
	public CertificateData(String certificateType, Date expiresOn, String username, PublicKey publicKey, byte[] signature) {
		this.certificateType = certificateType;
		this.expiresOn = expiresOn;
		this.username = username;
		this.publicKey = publicKey;
		this.signature = signature;
	}
	
	private CertificateData(GeneralizedSerializableObject object) {
		this(
			(String) object.getAttribute(kCertificateTypeAttrName),
			(Date) object.getAttribute(kExpiresAttrName),
			(String) object.getAttribute(kUsernameAttrName),
			(PublicKey) object.getAttribute(kPublicKeyAttrName),
			(byte[]) object.getAttribute(kSignatureAttrName));
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public Date getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(Date expiresOn) {
		this.expiresOn = expiresOn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	public void setSignature(PrivateKey privateKey) {
		try {
		//set signature to make sure certificate is not tempered or faked
		Signature signature = Signature.getInstance("SHA1withDSA");
		signature.initSign(privateKey);
		boolean bytesForSigning = true;
		byte[] bytesToSign = serializeObject(getGeneralizedObjectToSign(), bytesForSigning);
		signature.update(bytesToSign);
		this.signature = signature.sign();
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] getBytes() {
		return serializeObject(getGeneralizedObject(), false);
	}
	
	private GeneralizedSerializableObject getGeneralizedObjectToSign() {
		GeneralizedSerializableObject obj = new GeneralizedSerializableObject();
		obj.setAttribute(kExpiresAttrName, expiresOn);
		obj.setAttribute(kUsernameAttrName, username);
		return obj;
	}
	
	private GeneralizedSerializableObject getGeneralizedObject() {
		GeneralizedSerializableObject obj = getGeneralizedObjectToSign();
		obj.setAttribute(kCertificateTypeAttrName, certificateType);
		obj.setAttribute(kPublicKeyAttrName, publicKey);
		obj.setAttribute(kSignatureAttrName, signature);
		return obj;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}
	
	static byte[] serializeObject(Object obj, boolean bytesForSigning) {
		ByteArrayOutputStream byteArrayOS = null;
		try {
			if (obj instanceof GeneralizedSerializableObject) {
				GeneralizedSerializableObject gen = (GeneralizedSerializableObject)obj;
				byte[] strBytes = getSerializedDataBytes(gen);
				byteArrayOS = new ByteArrayOutputStream();
				byteArrayOS.write(strBytes);
				if (!bytesForSigning) {
					byteArrayOS.write(BYTES_SEPERATOR);	// seperator between the data and the signature.
					//	signature
					byte[] signature = (byte[])gen.getAttribute(kSignatureAttrName);
					if (signature != null) {
						byteArrayOS.write(signature);
					} //else {
						// System.out.println("Lenght of signature bytes = 0");
					//}
				}
				//	compute the bytes to be returned
				byteArrayOS.flush();
				byte[] returnBytes = byteArrayOS.toByteArray();
				return returnBytes;
			} else {
				return null;
			}
		} catch (IOException io) {
			throw new RuntimeException("Unable to serialize certificate");
		} finally {
			if (byteArrayOS != null) {
				try {
					byteArrayOS.close();
				} catch (Exception e) {
					// don't do anything - following statement is to avoid javalint errors.
					byteArrayOS = null;
				}
			}
		}
	}
	
	static byte[] getSerializedDataBytes(GeneralizedSerializableObject gen) {
		StringBuffer sb = new StringBuffer();
		Date expiryDate = (Date)(gen.getAttribute(kExpiresAttrName));
		String expiryDateInHex = Long.toHexString(expiryDate.getTime());
		sb.append(expiryDateInHex);
		sb.append(DATA_SEPERATOR);
		sb.append((String)gen.getAttribute(kUsernameAttrName));
		sb.append(DATA_SEPERATOR);

		//	take care of the other three attributes.
		sb.append((String)gen.getAttribute(kCertificateTypeAttrName));
		sb.append(DATA_SEPERATOR);
		byte[] strBytes = null;
		try {
			//	use UTF-8 encoding as the String can contain non-English chanracters
			strBytes = sb.toString().getBytes("UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported encoding encountered while converting certificate data.");
		}
		return strBytes;
	}
	
	public void verify(PublicKey key) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException,
		NoSuchProviderException, SignatureException {
		if(!key.equals(publicKey)) {
			throw new InvalidKeyException("Keys do not match!!");
		}
		Date now = new Date();
		if(now.after(expiresOn)) {
			throw new CertificateException("Expired certificate");
		}
		
		Signature sign = Signature.getInstance("SHA1withDSA");
		sign.initVerify(key);
		boolean bytesForSigning = true;
		byte[] byteToSign = serializeObject(getGeneralizedObjectToSign(), bytesForSigning);
		sign.update(byteToSign);
		boolean isValid = sign.verify(signature);
		if(!isValid) {
			throw new SignatureException();
		}
	}
	
	public static CertificateData getFromBytes(byte[] bytes) {
		GeneralizedSerializableObject obj =
			(GeneralizedSerializableObject) deserializeObject(bytes,
				GeneralizedSerializableObject.class);
		return new CertificateData(obj);
	}
	static Object deserializeObject(byte[] theBytes, Class certClass) {
		//	get the two portions of the certificate - data and the signature.
		boolean foundSeperator = false;
		int	seperatorPos = -1;
		for (int i = 0; i < theBytes.length; i++) {
			foundSeperator = identifyBytesSeperatorString(theBytes, i);
			if (foundSeperator) {
				seperatorPos = i;
				break;
			}
		}
		if (!(foundSeperator)) {
			throw new RuntimeException("Invalid Certificate. Did not the Bytes Seperator.");
		}
		GeneralizedSerializableObject deserializedObj = new GeneralizedSerializableObject();
		// deserialize the data portion.
		deserializeData(deserializedObj, theBytes, 0, seperatorPos - 1);
		//	deserialize the signature
		deserializeSignature(deserializedObj, theBytes, seperatorPos + BYTES_SEPERATOR.length, theBytes.length);
		//	this function "setupPK" needs the SabaSite Name. Hence, do this as the last step.
		setupPK(deserializedObj);
		return deserializedObj;
	}
	
	private static boolean identifyBytesSeperatorString(byte[] theBytes, int startIndex)
	{
		for (int j = 0; j < BYTES_SEPERATOR.length; j++) {
			if (theBytes[startIndex + j] != BYTES_SEPERATOR[j]) {
				return false;
			}
		}
		return true;
	}
	
	private static void deserializeData(GeneralizedSerializableObject deserializedObj,
			byte[] theBytes, int startIndex, int endIndex)
	{
		String dataString = null;
		try {
		//	use UTF-8 encoding as the bytes can contain non-English (international) characters
		dataString = new String(theBytes, startIndex, (endIndex - startIndex) + 1, "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
		throw new RuntimeException("Unsupported Encoding Exception received while deserializing data");
		}
		
		String[] tokens = dataString.split(DATA_SEPERATOR_REGEX);
		
		final int expiryDateIndex = 0;
		final int userNameIndex = 1;
		final int certTypeIndex = 2;
		//	get the individual components
		String expiryDateInHex = tokens[expiryDateIndex];
		long expiryDateVal = Long.parseLong(expiryDateInHex, 16);
		Date expiryDate = new Date();
		expiryDate.setTime(expiryDateVal);
		deserializedObj.setAttribute(kExpiresAttrName, expiryDate);
		
		String username = tokens[userNameIndex];
		deserializedObj.setAttribute(kUsernameAttrName, username);
		
		String certType = tokens[certTypeIndex];
		deserializedObj.setAttribute(kCertificateTypeAttrName, certType);
	}
	
	private static void deserializeSignature(GeneralizedSerializableObject deserializedObj,
			byte[] theBytes, int startIndex, int endIndex)
	{
		int signLength = endIndex - startIndex;
		ByteArrayInputStream byteArrIS = new ByteArrayInputStream(theBytes, startIndex, signLength);
		byte[] signature = new byte[signLength];
		int bytesRead = byteArrIS.read(signature, 0, signLength);
		
		if (bytesRead != signLength) {
		throw new RuntimeException("Error reading signature");
		}
		deserializedObj.setAttribute(kSignatureAttrName, signature);
	}
	
	private static void setupPK (GeneralizedSerializableObject deserializedObj)
	{
		String type = (String)deserializedObj.getAttribute(kCertificateTypeAttrName);
		PublicKey publicKey = null;
		if(type.equals(kCentroCertificateType))
		{
			publicKey = KeyStoreManager.getPublicKey();
		}
		/*else if (type.equals(kSabaSSOCertificateType))
		{
			//For SSO cetificate, we need to get the public key from database based on
			//it's domain id that is stored in the locale_id field in the certificateData.
			//String domainId = (String)deserializedObj.getAttribute(kLocaleAttrName);
			SSOKeyStoreManager mngr = new SSOKeyStoreManager(siteName);
            try {
                publicKey = mngr.getPublicKey();
            } catch (SabaException e) {
                Debug.trace("Error getting PublicKey", e);
                throw new RuntimeException("Error getting PublicKey");
            }
            if (publicKey == null)
                throw new RuntimeException("Error getting PublicKey");

		}*/
		deserializedObj.setAttribute(kPublicKeyAttrName, publicKey);
	}
}
