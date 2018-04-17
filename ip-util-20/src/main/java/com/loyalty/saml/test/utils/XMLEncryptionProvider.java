package com.loyalty.saml.test.utils;
/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2006 Sun Microsystems Inc. All Rights Reserved
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * https://opensso.dev.java.net/public/CDDLv1.0.html or
 * opensso/legal/CDDLv1.0.txt
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at opensso/legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * $Id: FMEncProvider.java,v 1.5 2008/06/25 05:48:03 qcheng Exp $
 *
 */

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.encryption.XMLEncryptionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <code>FMEncProvier</code> is a class for encrypting and decrypting XML
 * documents, it implements <code>EncProvider</code>.
 */

public final class XMLEncryptionProvider {

	/**
	 * A static map contains the recipients' entity IDs as the indices and
	 * symmetric keys as values. Symmetric key generation each time is expensive
	 * operation. Using the same key for each recipient is provided as an option
	 * here.
	 */
	static Hashtable cachedKeys = new Hashtable();

	/**
	 * A hidden property to switch between two encryption formats. If true, will
	 * have a ds:KeyInfo Element inside xenc:EncryptedData which will include
	 * the xenc:EncryptedKey Element (as defined in XML Encryption
	 * Specification). If false, will have xenc:EncryptedKey Element parallels
	 * to xenc:EncryptedData (as defined in SAML2 profile specification).
	 * Default to true if not specified.
	 */
	private static boolean encryptedKeyInKeyInfo = true;

	static {
		org.apache.xml.security.Init.init();
	}

	/**
	 * Encrypts the root element of the given XML document.
	 * 
	 * @param xmlString
	 *            String representing an XML document whose root element is to
	 *            be encrypted.
	 * @param recipientPublicKey
	 *            Public key used to encrypt the data encryption (secret) key,
	 *            it is the public key of the recipient of the XML document to
	 *            be encrypted.
	 * @param dataEncAlgorithm
	 *            Data encryption algorithm.
	 * @param dataEncStrength
	 *            Data encryption strength.
	 * @param recipientEntityID
	 *            Unique identifier of the recipient, it is used as the index to
	 *            the cached secret key so that the key can be reused for the
	 *            same recipient; It can be null in which case the secret key
	 *            will be generated every time and will not be cached and
	 *            reused. Note that the generation of a secret key is a
	 *            relatively expensive operation.
	 * @param outerElementName
	 *            Name of the element that will wrap around the encrypted data
	 *            and encrypted key(s) sub-elements
	 * @return org.w3c.dom.Element Root element of the encypted document; The
	 *         name of this root element is indicated by the last input
	 *         parameter
	 * @exception Exception
	 *                if there is an error during the encryption process
	 */
	public Element encrypt(String xmlString, Key recipientPublicKey, String dataEncAlgorithm, int dataEncStrength,
			String recipientEntityID, String outerElementName)

			throws Exception {

		return encrypt(xmlString, recipientPublicKey, null, dataEncAlgorithm, dataEncStrength, recipientEntityID,
				outerElementName);
	}

	/**
	 * Encrypts the root element of the given XML document.
	 * 
	 * @param xmlString
	 *            String representing an XML document whose root element is to
	 *            be encrypted.
	 * @param recipientPublicKey
	 *            Public key used to encrypt the data encryption (secret) key,
	 *            it is the public key of the recipient of the XML document to
	 *            be encrypted.
	 * @param secretKey
	 *            the secret key used to encrypted data.
	 * @param dataEncAlgorithm
	 *            Data encryption algorithm.
	 * @param dataEncStrength
	 *            Data encryption strength.
	 * @param recipientEntityID
	 *            Unique identifier of the recipient, it is used as the index to
	 *            the cached secret key so that the key can be reused for the
	 *            same recipient; It can be null in which case the secret key
	 *            will be generated every time and will not be cached and
	 *            reused. Note that the generation of a secret key is a
	 *            relatively expensive operation.
	 * @param outerElementName
	 *            Name of the element that will wrap around the encrypted data
	 *            and encrypted key(s) sub-elements
	 * @return org.w3c.dom.Element Root element of the encypted document; The
	 *         name of this root element is indicated by the last input
	 *         parameter
	 * @exception Exception
	 *                if there is an error during the encryption process
	 */
	public Element encrypt(String xmlString, Key recipientPublicKey, SecretKey secretKey, String dataEncAlgorithm,
			int dataEncStrength, String recipientEntityID, String outerElementName)

			throws Exception {

		String classMethod = "FMEncProvider.encrypt: ";

		// checking the input parameters
		if (xmlString == null || xmlString.length() == 0 || recipientPublicKey == null || dataEncAlgorithm == null
				|| dataEncAlgorithm.length() == 0 || outerElementName == null || outerElementName.length() == 0) {

			System.out.println(classMethod + "Null input parameter(s).");
			throw new Exception("Null input parameter(s).");
		}
		if (!dataEncAlgorithm.equals(XMLCipher.AES_128) && !dataEncAlgorithm.equals(XMLCipher.AES_192)
				&& !dataEncAlgorithm.equals(XMLCipher.AES_256) && !dataEncAlgorithm.equals(XMLCipher.TRIPLEDES)) {

			throw new Exception("unsupportedKeyAlg");
		}
		if ((dataEncAlgorithm.equals(XMLCipher.AES_128) && dataEncStrength != 128)
				|| (dataEncAlgorithm.equals(XMLCipher.AES_192) && dataEncStrength != 192)
				|| (dataEncAlgorithm.equals(XMLCipher.AES_256) && dataEncStrength != 256)) {
			throw new Exception(classMethod + "Data encryption algorithm " + dataEncAlgorithm + "and strength "
					+ dataEncStrength + " mismatch.");
		}
		Document doc = XMLUtils.toDOMDocument(xmlString);
		if (doc == null) {
			throw new Exception("errorObtainingElement");
		}
		if (dataEncStrength <= 0) {
			dataEncStrength = 128;
		}
		Element rootElement = doc.getDocumentElement();
		if (rootElement == null) {
			throw new Exception(classMethod + "Empty document.");
		}
		// start of obtaining secret key
		if (secretKey == null) {
			if (recipientEntityID != null) {
				if (cachedKeys.containsKey(recipientEntityID)) {
					secretKey = (SecretKey) cachedKeys.get(recipientEntityID);
				} else {
					secretKey = generateSecretKey(dataEncAlgorithm, dataEncStrength);
					cachedKeys.put(recipientEntityID, secretKey);
				}
			} else {
				secretKey = generateSecretKey(dataEncAlgorithm, dataEncStrength);
			}
			if (secretKey == null) {
				throw new Exception("errorGenerateKey");
			}
		}
		// end of obtaining secret key

		XMLCipher cipher = null;
		// start of encrypting the secret key with public key
		String publicKeyEncAlg = recipientPublicKey.getAlgorithm();
		/*
		 * note that the public key encryption algorithm could only have three
		 * possible values here: "RSA", "AES", "DESede"
		 */
		try {
			if (publicKeyEncAlg.equals(EncryptionConstants.RSA)) {
				cipher = XMLCipher.getInstance(XMLCipher.RSA_v1dot5);

			} else if (publicKeyEncAlg.equals(EncryptionConstants.TRIPLEDES)) {
				cipher = XMLCipher.getInstance(XMLCipher.TRIPLEDES_KeyWrap);

			} else if (publicKeyEncAlg.equals(EncryptionConstants.AES)) {

				cipher = XMLCipher.getInstance(XMLCipher.AES_128_KeyWrap);
			} else {
				throw new Exception("unsupportedKeyAlg");
			}
		} catch (XMLEncryptionException xe1) {
			throw new Exception(classMethod + "Unable to obtain cipher with public key algorithm.", xe1);
		}
		try {
			cipher.init(XMLCipher.WRAP_MODE, recipientPublicKey);
		} catch (XMLEncryptionException xe2) {
			throw new Exception(classMethod + "Failed to initialize cipher with public key", xe2);
		}
		EncryptedKey encryptedKey = null;
		try {
			encryptedKey = cipher.encryptKey(doc, secretKey);
		} catch (XMLEncryptionException xe3) {
			throw new Exception(classMethod + "Failed to encrypt secret key with public key", xe3);
		}
		// end of encrypting the secret key with public key

		// start of doing data encryption
		try {
			cipher = XMLCipher.getInstance(dataEncAlgorithm);
		} catch (XMLEncryptionException xe4) {
			throw new Exception(
					classMethod + "Failed to obtain a cipher for " + "data encryption algorithm" + dataEncAlgorithm,
					xe4);
		}
		try {
			cipher.init(XMLCipher.ENCRYPT_MODE, secretKey);
		} catch (XMLEncryptionException xe5) {
			throw new Exception(classMethod + "Failed to initialize cipher with secret key.", xe5);
		}
		Document resultDoc = null;
		try {
			resultDoc = cipher.doFinal(doc, rootElement);
		} catch (Exception e) {
			throw new Exception(classMethod + "Failed to do the final data encryption.", e);
		}
		// end of doing data encryption

		// add the EncryptedKey element
		Element ek = null;
		try {
			ek = cipher.martial(doc, encryptedKey);
		} catch (Exception xe6) {
			throw new Exception(classMethod + "Failed to martial the encrypted key", xe6);
		}

		String outerElemNS = SAML2Constants.ASSERTION_NAMESPACE_URI;
		String outerElemPrefix = "saml";
		if (outerElementName.equals("NewEncryptedID")) {
			outerElemNS = SAML2Constants.PROTOCOL_NAMESPACE;
			outerElemPrefix = "samlp";
		}
		Element outerElement = resultDoc.createElementNS(outerElemNS, outerElemPrefix + ":" + outerElementName);
		outerElement.setAttributeNS(SAML2Constants.NS_XML, "xmlns:" + outerElemPrefix, outerElemNS);
		Element ed = resultDoc.getDocumentElement();
		resultDoc.replaceChild(outerElement, ed);
		outerElement.appendChild(ed);
		if (encryptedKeyInKeyInfo) {
			// create a ds:KeyInfo Element to include the EncryptionKey
			Element dsElement = resultDoc.createElementNS(SAML2Constants.NS_XMLSIG, "ds:KeyInfo");
			dsElement.setAttributeNS(SAML2Constants.NS_XML, "xmlns:ds", SAML2Constants.NS_XMLSIG);
			dsElement.appendChild(ek);
			// find the xenc:CipherData Element inside the encrypted data
			NodeList nl = ed.getElementsByTagNameNS(SAML2Constants.NS_XMLENC, "CipherData");
			if ((nl == null) || (nl.getLength() == 0)) {
				throw new Exception(classMethod + "Unable to find required xenc:CipherData Element.");
			}
			Element cipherDataElement = (Element) nl.item(0);
			// insert the EncryptedKey before the xenc:CipherData Element
			ed.insertBefore(dsElement, cipherDataElement);
		} else {
			outerElement.appendChild(ek);
		}
		return resultDoc.getDocumentElement();
	}

	/**
	 * Returns the secret key that encrypts encrypted data and is encrypted with
	 * recipient's public key in the XML document.
	 * 
	 * @param xmlString
	 *            String representing an XML document with encrypted secret key.
	 * @param recipientPrivateKey
	 *            Private key used to decrypt the secret key
	 * @return the secret key.
	 * @exception Exception
	 *                if there is an error during the decryption process
	 */
	public SecretKey getSecretKey(String xmlString, Key recipientPrivateKey)

			throws Exception {

		String classMethod = "FMEncProvider.getSecretKey: ";

		if (xmlString == null || xmlString.length() == 0 || recipientPrivateKey == null) {
			throw new Exception(classMethod + "nullInput");
		}
		Document doc = XMLUtils.toDOMDocument(xmlString);
		if (doc == null) {
			throw new Exception(classMethod + "errorObtainingElement");
		}
		Element rootElement = doc.getDocumentElement();
		if (rootElement == null) {
			throw new Exception(classMethod + "Empty document.");
		}
		Element firstChild = getNextElementNode(rootElement.getFirstChild());
		if (firstChild == null) {
			throw new Exception(classMethod + "Missing the EncryptedData element.");
		}
		Element secondChild = getNextElementNode(firstChild.getNextSibling());
		if (secondChild == null) {
			NodeList nl = firstChild.getElementsByTagNameNS(SAML2Constants.NS_XMLENC, "EncryptedKey");
			if ((nl == null) || (nl.getLength() == 0)) {
				throw new Exception(classMethod + "Missing the EncryptedKey element.");
			} else {
				// use the first EncryptedKey found
				secondChild = (Element) nl.item(0);
			}
		}

		XMLCipher cipher = null;
		try {
			cipher = XMLCipher.getInstance();
		} catch (XMLEncryptionException xe1) {
			throw new Exception(classMethod + "Unable to get a cipher instance.", xe1);
		}
		try {
			cipher.init(XMLCipher.DECRYPT_MODE, null);
		} catch (XMLEncryptionException xe2) {
			throw new Exception(classMethod + "Failed to initialize cipher for decryption mode", xe2);
		}
		EncryptedData encryptedData = null;
		try {
			encryptedData = cipher.loadEncryptedData(doc, firstChild);
		} catch (XMLEncryptionException xe3) {
			throw new Exception(classMethod + "Failed to load encrypted data", xe3);
		}
		EncryptedKey encryptedKey = null;
		try {
			encryptedKey = cipher.loadEncryptedKey(doc, secondChild);
		} catch (XMLEncryptionException xe4) {
			throw new Exception(classMethod + "Failed to load encrypted key", xe4);
		}
		Document decryptedDoc = null;
		if ((encryptedKey != null) && (encryptedData != null)) {
			XMLCipher keyCipher = null;
			try {
				keyCipher = XMLCipher.getInstance();
			} catch (XMLEncryptionException xe5) {
				throw new Exception(classMethod + "Failed to get a cipher instance " + "for decrypting secret key.",
						xe5);
			}
			try {
				keyCipher.init(XMLCipher.UNWRAP_MODE, recipientPrivateKey);
			} catch (XMLEncryptionException xe6) {
				throw new Exception(classMethod + "Failed to initialize cipher in unwrap mode " + "with private key",
						xe6);
			}

			try {
				return (SecretKey) keyCipher.decryptKey(encryptedKey,
						encryptedData.getEncryptionMethod().getAlgorithm());
			} catch (XMLEncryptionException xe7) {
				throw new Exception(classMethod + "Failed to decrypt the secret key", xe7);
			}
		}
		return null;
	}

	/**
	 * Decrypts an XML document that contains encrypted data.
	 * 
	 * @param xmlString
	 *            String representing an XML document with encrypted data.
	 * @param recipientPrivateKey
	 *            Private key used to decrypt the secret key
	 * @return org.w3c.dom.Element Decrypted XML document. For example, if the
	 *         input document's root element is EncryptedID, then the return
	 *         element will be NameID
	 * @exception Exception
	 *                if there is an error during the decryption process
	 */
	public Element decrypt(String xmlString, Key recipientPrivateKey)

			throws Exception {

		String classMethod = "FMEncProvider.decrypt: ";
		if (xmlString == null || xmlString.length() == 0 || recipientPrivateKey == null) {
			throw new Exception(classMethod + "nullInput");
		}
		Document doc = XMLUtils.toDOMDocument(xmlString);
		if (doc == null) {
			throw new Exception(classMethod + "errorObtainingElement");
		}
		Element rootElement = doc.getDocumentElement();
		if (rootElement == null) {
			throw new Exception(classMethod + "Empty document.");
		}
		Element firstChild = getNextElementNode(rootElement.getFirstChild());
		if (firstChild == null) {
			throw new Exception(classMethod + "Missing the EncryptedData element.");
		}
		Element secondChild = getNextElementNode(firstChild.getNextSibling());
		if (secondChild == null) {
			NodeList nl = firstChild.getElementsByTagNameNS(SAML2Constants.NS_XMLENC, "EncryptedKey");
			if ((nl == null) || (nl.getLength() == 0)) {
				throw new Exception(classMethod + "Missing the EncryptedKey element.");
			} else {
				// use the first EncryptedKey found
				secondChild = (Element) nl.item(0);
			}
		}
		XMLCipher cipher = null;
		try {
			cipher = XMLCipher.getInstance();
		} catch (XMLEncryptionException xe1) {
			throw new Exception(classMethod + "Unable to get a cipher instance.", xe1);
		}
		try {
			cipher.init(XMLCipher.DECRYPT_MODE, null);
		} catch (XMLEncryptionException xe2) {
			throw new Exception(classMethod + "Failed to initialize cipher for decryption mode", xe2);
		}
		EncryptedData encryptedData = null;
		try {
			encryptedData = cipher.loadEncryptedData(doc, firstChild);
		} catch (XMLEncryptionException xe3) {
			throw new Exception(classMethod + "Failed to load encrypted data", xe3);
		}
		EncryptedKey encryptedKey = null;
		try {
			encryptedKey = cipher.loadEncryptedKey(doc, secondChild);
		} catch (XMLEncryptionException xe4) {
			throw new Exception(classMethod + "Failed to load encrypted key", xe4);
		}
		Document decryptedDoc = null;
		if (encryptedKey != null && encryptedData != null) {
			XMLCipher keyCipher = null;
			try {
				keyCipher = XMLCipher.getInstance();
			} catch (XMLEncryptionException xe5) {
				throw new Exception(classMethod + "Failed to get a cipher instance " + "for decrypting secret key.",
						xe5);
			}
			try {
				keyCipher.init(XMLCipher.UNWRAP_MODE, recipientPrivateKey);
			} catch (XMLEncryptionException xe6) {
				throw new Exception(classMethod + "Failed to initialize cipher in unwrap mode " + "with private key",
						xe6);
			}
			Key encryptionKey = null;
			try {
				// TODO: not sure about the algorithm here
				encryptionKey = keyCipher.decryptKey(encryptedKey, encryptedData.getEncryptionMethod().getAlgorithm());
			} catch (XMLEncryptionException xe7) {
				throw new Exception(classMethod + "Failed to decrypt the secret key", xe7);
			}
			cipher = null;
			try {
				cipher = XMLCipher.getInstance();
			} catch (XMLEncryptionException xe8) {
				throw new Exception(classMethod + "Failed to get cipher instance for " + "final data decryption.", xe8);
			}
			try {
				cipher.init(XMLCipher.DECRYPT_MODE, encryptionKey);
			} catch (XMLEncryptionException xe9) {
				throw new Exception(classMethod + "Failed to initialize cipher with secret key.", xe9);
			}
			try {
				decryptedDoc = cipher.doFinal(doc, firstChild);
			} catch (Exception e) {
				throw new Exception(classMethod + "Failed to decrypt data.", e);
			}
		}
		Element root = decryptedDoc.getDocumentElement();
		Element child = getNextElementNode(root.getFirstChild());
		if (child == null) {
			throw new Exception(classMethod + "decrypted document contains empty element.");
		}
		root.removeChild(child);
		decryptedDoc.replaceChild(child, root);
		return decryptedDoc.getDocumentElement();
	}

	/**
	 * Returns the next Element node, return null if no such node exists.
	 */
	private Element getNextElementNode(Node node) {
		while (true) {
			if (node == null) {
				return null;
			} else if (node.getNodeType() == Node.ELEMENT_NODE) {
				return (Element) node;
			} else {
				node = node.getNextSibling();
			}
		}
	}

	/**
	 * Generates secret key for a given algorithm and key strength.
	 */
	private SecretKey generateSecretKey(String algorithm, int keyStrength) throws Exception {
		KeyGenerator keygen = null;
		try {
			if (algorithm.equals(XMLCipher.AES_128) || algorithm.equals(XMLCipher.AES_192)
					|| algorithm.equals(XMLCipher.AES_256)) {
				keygen = KeyGenerator.getInstance("AES");
			} else if (algorithm.equals(XMLCipher.TRIPLEDES)) {
				keygen = KeyGenerator.getInstance("TripleDES");
			} else {
				throw new Exception("unsupportedKeyAlg");
			}

			if (keyStrength != 0) {
				keygen.init(keyStrength);
			}
		} catch (NoSuchAlgorithmException ne) {
			throw new Exception(ne);
		}

		return (keygen != null) ? keygen.generateKey() : null;
	}
}
