package com.loyalty.saml.test.utils;

import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KeyStoreHelper implements KeyProvider {

	final static Logger log = LoggerFactory.getLogger(KeyStoreHelper.class);
	
	public KeyStore getKeyStore() {
		return getKeyStore(KeyStoreConstants.KEYSTORE_NAME, KeyStoreConstants.KEYSTORE_PASS);
	}

	public KeyStore getKeyStore(String keystoreName, String keystorePass) {

		KeyStore ks = null;
		try {
			if (keystoreName == null || keystorePass == null)
				throw new IllegalArgumentException();
			ks = KeyStore.getInstance(KeyStore.getDefaultType());
			InputStream fis = this.getClass().getClassLoader().getResourceAsStream(keystoreName);
			ks.load(fis, keystorePass.toCharArray());
		} catch (Exception e) {
			log.error("Exception in getting keystore:", e);
		}
		return ks;
	}

	public PrivateKey getPrivateKey(String storeName, String storePass, String keyAlias, String keyPass) {
		PrivateKey key = null;
		try {
			if (keyAlias == null)
				throw new IllegalArgumentException();
			KeyStore keystore = getKeyStore(storeName, storePass);
			if (keyPass == null)
				keyPass = storePass;
			Key privateKey = keystore.getKey(keyAlias, keyPass.toCharArray());
			if (PrivateKey.class.isInstance(privateKey)) {
				key = (PrivateKey) privateKey;
			}
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
		}
		return key;
	}

	public PrivateKey getPrivateKey(String keyAlias, String keyPass) {
		return getPrivateKey(KeyStoreConstants.KEYSTORE_NAME, KeyStoreConstants.KEYSTORE_PASS, keyAlias, keyPass);
	}

	public PrivateKey getPrivateKey(String keyAlias) {
		return getPrivateKey(KeyStoreConstants.KEYSTORE_NAME, KeyStoreConstants.KEYSTORE_PASS, keyAlias, null);
	}

	public PublicKey getPublicKey(String storeName, String storePass, String keyAlias) {
		PublicKey key = null;
		try {
			if (keyAlias == null)
				throw new IllegalArgumentException();
			KeyStore keystore = getKeyStore(storeName, storePass);
			Certificate cert = keystore.getCertificate(keyAlias);
			key = cert.getPublicKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}

	public PublicKey getPublicKey(String keyAlias) {
		return getPublicKey(KeyStoreConstants.KEYSTORE_NAME, KeyStoreConstants.KEYSTORE_PASS, keyAlias);
	}

	public Certificate getCertificate(String storeName, String storePass, String keyAlias) {
		Certificate cert = null;
		try {
			if (keyAlias == null)
				throw new IllegalArgumentException();
			KeyStore keystore = getKeyStore(storeName, storePass);
			cert = keystore.getCertificate(keyAlias);
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
		}
		return cert;
	}

	public Certificate getCertificate(String keyAlias) {
		return getCertificate(KeyStoreConstants.KEYSTORE_NAME, KeyStoreConstants.KEYSTORE_PASS, keyAlias);
	}

}
