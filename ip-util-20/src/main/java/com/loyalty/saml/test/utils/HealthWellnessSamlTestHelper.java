package com.loyalty.saml.test.utils;

import java.security.Key;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.xml.security.encryption.XMLCipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class HealthWellnessSamlTestHelper {

	private static String RANDOM_STRING = "abcdefghijklmnopqrstuvwxyz1234567890";

	final static Logger log = LoggerFactory.getLogger(HealthWellnessSamlTestHelper.class);
	
	public static String createSamlToken(String attributeStatement, String authURL, String issuer,
			String privateKeyForSigning, String publicKeyForEncryptingAssertion, String nameID) {
			log.debug("attributeStatement = " + attributeStatement + "\n");

		// Create a SAML assertion, inserting the attributeStatement that we
		// allowed user to submit
		// This could be refactored to allow each field to be submitted
		Date now = new Date();
		String samlXml = generateSamlAssertion(attributeStatement, now, authURL, issuer, nameID);
		log.debug("SAML XML = " + samlXml + "\n");

		// Sign the Assertion with the private key
		KeyStoreHelper ksh = new KeyStoreHelper();
		if (privateKeyForSigning != null && privateKeyForSigning.length() > 0) {
			samlXml = signSamlXml(ksh, samlXml, privateKeyForSigning);
			log.debug("Signed = " + samlXml + "\n");
		}

		if (publicKeyForEncryptingAssertion != null && publicKeyForEncryptingAssertion.length() > 0) {
			// Encrypt the Assertion with the public key (jtwb requires this)
			Key key = ksh.getPublicKey(publicKeyForEncryptingAssertion);
			samlXml = encryptSamlAssertion(samlXml, key);
			log.debug("Encrypted Assertion = " + samlXml + "\n");
		}

		// Place the assertion into a SAML response
		String response = generateSamlResponse(samlXml, now, authURL, issuer);
		log.debug("Saml Response XML = " + response + "\n");

		// Base64 encode the SAML response
		String base64Encode = Base64.encode(response.getBytes());
		log.debug("Base64 encoded Token = " + base64Encode + "\n");

		return base64Encode;
	}

	private static String generateSamlAssertion(String attributeStatement, Date now, String authURL, String issuer,
			String nameID) {

		Map<String, Object> map = new HashMap<String, Object>();

		// SAML data
		// payload
		map.put("attributeStatement", attributeStatement);
		// time information
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		map.put("timestamp", df.format(cal.getTime()));
		cal.add(Calendar.SECOND, -30);
		map.put("timestampMinusHalfMinute", df.format(cal.getTime()));
		cal.add(Calendar.SECOND, 75);
		map.put("timestampPlusOneMinute", df.format(cal.getTime()));
		// miscellaneous values
		map.put("assetionId", RandomStringUtils.random(42, RANDOM_STRING));
		map.put("name", RandomStringUtils.randomAlphanumeric(28));
		map.put("session", RandomStringUtils.random(42, RANDOM_STRING));
		map.put("authURL", authURL);
		map.put("issuer", issuer);

		if (nameID != null) {
			map.put("nameID", "<saml:NameID>" + nameID + "</saml:NameID>");
		} else {
			map.put("nameID", "<saml:NameID>17f59429-ebd4-4617-a019-001df8731658</saml:NameID>");
		}

		// merge data into XML template
		String samlXml = VelocityHelper.processTemplate("velocity/samlAssertionHW.vm", map);

		return samlXml;
	}

	private static String signSamlXml(KeyStoreHelper ksh, String samlXml, String privateKeyAlias) {

		String signature = "XML Signing FAILED";
		XMLSignatureProvider sigProv = new XMLSignatureProvider();
		sigProv.initialize(ksh);
		try {
			signature = sigProv.signXML(samlXml, privateKeyAlias);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
		}
		return signature;
	}

	public static String encryptSamlAssertion(String samlXml, Key key) {

		String encrypt = "";
		XMLEncryptionProvider enc = new XMLEncryptionProvider();
		try {
			// TODO: Don't send in recipientEntityID?
			// Element encryptXml = enc.encrypt(samlXml, key, XMLCipher.AES_256,
			// 256, "ALG_OMA10_SP", "EncryptedAssertion");
			Element encryptXml = enc.encrypt(samlXml, key, XMLCipher.AES_256, 256, null, "EncryptedAssertion");
			encrypt = XMLUtils.print(encryptXml);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return encrypt;
	}

	private static String generateSamlResponse(String encrypted, Date now, String authURL, String issuer) {

		Map<String, Object> map = new HashMap<String, Object>();

		// SAML data
		// time information
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		map.put("timestamp", df.format(cal.getTime()));
		// miscellaneous values
		map.put("responseId", RandomStringUtils.random(42, RANDOM_STRING));
		map.put("encryptedAssertion", encrypted);
		map.put("authURL", authURL);
		map.put("issuer", issuer);

		// merge data into XML template
		String samlXml = VelocityHelper.processTemplate("velocity/samlResponseHW.vm", map);

		return samlXml;
	}

}
