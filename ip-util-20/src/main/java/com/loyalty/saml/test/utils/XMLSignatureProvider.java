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
 * $Id: AMSignatureProvider.java,v 1.11 2009/08/29 03:06:47 mallas Exp $
 *
 */

package com.loyalty.saml.test.utils;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;

import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.keyvalues.DSAKeyValue;
import org.apache.xml.security.keys.content.keyvalues.RSAKeyValue;
import org.apache.xml.security.keys.keyresolver.implementations.X509CertificateResolver;
import org.apache.xml.security.keys.keyresolver.implementations.X509IssuerSerialResolver;
import org.apache.xml.security.keys.keyresolver.implementations.X509SKIResolver;
import org.apache.xml.security.keys.keyresolver.implementations.X509SubjectNameResolver;
import org.apache.xml.security.keys.storage.StorageResolver;
import org.apache.xml.security.keys.storage.implementations.KeyStoreResolver;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.IdResolver;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <code>SignatureProvider</code> is an interface to be implemented to sign and
 * verify xml signature
 * <p>
 */

public class XMLSignatureProvider {
	protected KeyProvider keystore = null;
	private String c14nMethod = null;
	private String transformAlg = null;
	// define default id attribute name
	private static final String DEF_ID_ATTRIBUTE = "id";
	// flag to check if the partner's signing cert is in the keystore.
	private boolean isJKSKeyStore = true;
	private String wsfVersion = null;
	private String defaultSigAlg = null;

	private String elementToSign = "Issuer";

	/**
	 * Default Constructor
	 */
	public XMLSignatureProvider() {
		org.apache.xml.security.Init.init();

		c14nMethod = SAMLConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS;
		transformAlg = SAMLConstants.TRANSFORM_C14N_EXCL_OMIT_COMMENTS;
		defaultSigAlg = "";

	}

	/**
	 * Constructor
	 */
	public void initialize(KeyProvider keyProvider) {
		keystore = keyProvider;
	}

	/**
	 * Sign the xml document using enveloped signatures.
	 * 
	 * @param doc
	 *            XML dom object
	 * @param certAlias
	 *            Signer's certificate alias name
	 * @return signature Element object
	 * @throws Exception
	 *             if the document could not be signed
	 */
	public org.w3c.dom.Element signXML(org.w3c.dom.Document doc, java.lang.String certAlias) throws Exception {
		return signXML(doc, certAlias, null);
	}

	/**
	 * Sign the xml document using enveloped signatures.
	 * 
	 * @param doc
	 *            XML dom object
	 * @param certAlias
	 *            Signer's certificate alias name
	 * @param algorithm
	 *            XML signature algorithm
	 * @return signature dom object
	 * @throws Exception
	 *             if the document could not be signed
	 */
	public org.w3c.dom.Element signXML(org.w3c.dom.Document doc, java.lang.String certAlias, java.lang.String algorithm)
			throws Exception {
		if (doc == null) {
			throw new Exception("signXML: doc is null.");
		}
		if (certAlias == null || certAlias.length() == 0) {
			throw new Exception("signXML: certAlias is null.");
		}
		org.w3c.dom.Element root = null;
		XMLSignature sig = null;
		try {
			Constants.setSignatureSpecNSprefix("");
			if (keystore == null) {
				throw new Exception("signXML: keystore is null.");
			}
			PrivateKey privateKey = (PrivateKey) keystore.getPrivateKey(certAlias);

			if (privateKey == null) {
				throw new Exception("signXML: private key is null");
			}
			root = doc.getDocumentElement();

			if (algorithm == null || algorithm.length() == 0) {
				algorithm = getKeyAlgorithm(privateKey);
			}
			if (!isValidAlgorithm(algorithm)) {
				throw new Exception("signXML: invalid algorithm");
			}

			if (c14nMethod == null || c14nMethod.length() == 0) {
				sig = new XMLSignature(doc, "", algorithm);
			} else {
				if (!isValidCanonicalizationMethod(c14nMethod)) {
					throw new Exception("signXML: invalidCanonicalizationMethod");
				}
				sig = new XMLSignature(doc, "", algorithm, c14nMethod);
			}
			root.insertBefore(sig.getElement(), root.getFirstChild().getNextSibling().getNextSibling());

			// do transform
			Transforms transforms = new Transforms(doc);
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			// If exclusive canonicalization is presented in the saml locale
			// file, we will add a transform for it. Otherwise, will not do
			// such transform due to performance reason.
			if (transformAlg != null && transformAlg.length() != 0) {
				if (!isValidTransformAlgorithm(transformAlg)) {
					throw new Exception("signXML: invalid Transform Algorithm");
				}
				transforms.addTransform(transformAlg);
			}

			// sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);
			String refId = root.getAttribute("ID");
			sig.addDocument("#" + refId, transforms, Constants.ALGO_ID_DIGEST_SHA1);

			// add certificate
			X509Certificate cert = (X509Certificate) keystore.getCertificate(certAlias);
			sig.addKeyInfo(cert);
			sig.sign(privateKey);
		} catch (Exception e) {
			throw new Exception("signXML Exception: " + e.getMessage());
		}
		return (doc.getDocumentElement());
	}

	/**
	 * Sign the xml string using enveloped signatures.
	 * 
	 * @param xmlString
	 *            xml string to be signed
	 * @param certAlias
	 *            Signer's certificate alias name
	 * @return XML signature string
	 * @throws Exception
	 *             if the xml string could not be signed
	 */
	public java.lang.String signXML(java.lang.String xmlString, java.lang.String certAlias) throws Exception {
		return signXML(xmlString, certAlias, null);
	}

	/**
	 * Sign the xml string using enveloped signatures.
	 * 
	 * @param xmlString
	 *            xml string to be signed
	 * @param certAlias
	 *            Signer's certificate alias name
	 * @param algorithm
	 *            XML Signature algorithm
	 * @return XML signature string
	 * @throws Exception
	 *             if the xml string could not be signed
	 */
	public java.lang.String signXML(java.lang.String xmlString, java.lang.String certAlias, java.lang.String algorithm)
			throws Exception {
		if (xmlString == null || xmlString.length() == 0) {
			throw new Exception("signXML: xmlString is null.");
		}
		if (certAlias == null || certAlias.length() == 0) {
			throw new Exception("signXML: certAlias is null.");
		}
		Element el = null;
		try {
			Document doc = XMLUtils.toDOMDocument(xmlString);
			el = signXML(doc, certAlias, algorithm);
		} catch (Exception e) {
			throw new Exception("signXML Exception: " + e.getMessage());
		}

		return XMLUtils.print(el);
	}

	/**
	 * Verify all the signatures of the xml document
	 * 
	 * @param doc
	 *            XML dom document whose signature to be verified
	 * @param certAlias
	 *            certAlias alias for Signer's certificate, this is used to
	 *            search signer's public certificate if it is not presented in
	 *            ds:KeyInfo
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(Document doc, String certAlias) throws Exception {

		return verifyXMLSignature(SOAPBindingConstants.WSF_10_VERSION, certAlias, doc);
	}

	/**
	 * Verify all the signatures of the xml document
	 * 
	 * @param wsfVersion
	 *            the web services version.
	 * @param doc
	 *            XML dom document whose signature to be verified
	 * @param certAlias
	 *            certAlias alias for Signer's certificate, this is used to
	 *            search signer's public certificate if it is not presented in
	 *            ds:KeyInfo
	 * @return true if the xml signature is verified, false otherwise
	 * @exception Exception
	 *                if problem occurs during verification
	 */
	public boolean verifyXMLSignature(String wsfVersion, String certAlias, Document doc) throws Exception {

		if (doc == null) {
			throw new Exception("verifyXMLSignature: document is null.");
		}

		try {
			this.wsfVersion = wsfVersion;
			String wsuNS = SAMLConstants.NS_WSU;
			String wsseNS = SAMLConstants.NS_WSSE;

			if ((wsfVersion != null) && (wsfVersion.equals(SOAPBindingConstants.WSF_11_VERSION))) {
				wsuNS = WSSEConstants.NS_WSU_WSF11;
				wsseNS = WSSEConstants.NS_WSSE_WSF11;
			}

			Element wsucontext = XMLUtils.createDSctx(doc, "wsu", wsuNS);

			NodeList wsuNodes = (NodeList) XPathAPI.selectNodeList(doc, "//*[@wsu:Id]", wsucontext);

			if ((wsuNodes != null) && (wsuNodes.getLength() != 0)) {
				for (int i = 0; i < wsuNodes.getLength(); i++) {
					Element elem = (Element) wsuNodes.item(i);
					String id = elem.getAttributeNS(wsuNS, "Id");
					if ((id != null) && (id.length() != 0)) {
						IdResolver.registerElementById(elem, id);
					}
				}
			}

			String[] attrs = { "AssertionID", "RequestID", "ResponseID" };
			for (int j = 0; j < attrs.length; j++) {
				NodeList aList = (NodeList) XPathAPI.selectNodeList(doc, "//*[@" + attrs[j] + "]");
				if ((aList != null) && (aList.getLength() != 0)) {
					int len = aList.getLength();
					for (int i = 0; i < len; i++) {
						Element elem = (Element) aList.item(i);
						String id = elem.getAttribute(attrs[j]);
						if (id != null && id.length() != 0) {
							IdResolver.registerElementById(elem, id);
						}
					}
				}
			}

			Element nscontext = XMLUtils.createDSctx(doc, "ds", Constants.SignatureSpecNS);
			NodeList sigElements = XPathAPI.selectNodeList(doc, "//ds:Signature", nscontext);
			X509Certificate newcert = (X509Certificate) keystore.getCertificate(certAlias);
			PublicKey key = keystore.getPublicKey(certAlias);
			Element sigElement = null;
			// loop
			for (int i = 0; i < sigElements.getLength(); i++) {
				sigElement = (Element) sigElements.item(i);
				XMLSignature signature = new XMLSignature(sigElement, "");
				KeyInfo ki = signature.getKeyInfo();
				PublicKey pk = this.getX509PublicKey(doc, ki);
				if (pk != null) {
					// verify using public key
					if (!signature.checkSignatureValue(pk)) {
						return false;
					}
				} else {
					if (certAlias == null || certAlias.equals("")) {
						return false;
					}
					if (newcert != null) {
						if (!signature.checkSignatureValue(newcert)) {
							return false;
						}
					} else {
						if (key != null) {
							if (!signature.checkSignatureValue(key)) {
								return false;
							}
						} else {
							return false;
						}
					}
				}
			}
			return true;
		} catch (Exception ex) {
			throw new Exception("verifyXMLSignature Exception: " + ex.getMessage());
		}
	}

	/**
	 * Verify the signature of the xml document
	 * 
	 * @param doc
	 *            XML dom document whose signature to be verified
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */

	public boolean verifyXMLSignature(org.w3c.dom.Document doc) throws Exception {
		if (doc == null) {
			throw new Exception("verifyXMLSignature: document is null.");
		}
		return verifyXMLSignature(doc, (String) null);
	}

	/**
	 * Verify the signature of the xml element.
	 *
	 * @param element
	 *            XML dom element whose signature to be verified
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(org.w3c.dom.Element element) throws Exception {
		if (element == null) {
			throw new Exception("signXML: element is null.");
		}
		return verifyXMLSignature(XMLUtils.print(element));
	}

	/**
	 * Verify the signature of the xml document
	 * 
	 * @param element
	 *            XML Element whose signature to be verified
	 * @param certAlias
	 *            certAlias alias for Signer's certificate, this is used to
	 *            search signer's public certificate if it is not presented in
	 *            ds:KeyInfo
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(org.w3c.dom.Element element, java.lang.String certAlias) throws Exception {
		return verifyXMLSignature(element, DEF_ID_ATTRIBUTE, certAlias);
	}

	/**
	 * Verify the signature of the xml document
	 * 
	 * @param element
	 *            XML Element whose signature to be verified
	 * @param idAttrName
	 *            Attribute name for the id attribute
	 * @param certAlias
	 *            certAlias alias for Signer's certificate, this is used to
	 *            search signer's public certificate if it is not presented in
	 *            ds:KeyInfo
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(org.w3c.dom.Element element, java.lang.String idAttrName,
			java.lang.String certAlias) throws Exception {
		if (element == null) {
			throw new Exception("signXML: element is null.");
		}
		Document doc = null;
		try {
			doc = XMLUtils.newDocument();
			doc.appendChild(doc.importNode(element, true));
		} catch (Exception ex) {
			throw new Exception("verifyXMLSignature Exception: " + ex.getMessage());
		}

		return verifyXMLSignature(doc, idAttrName, certAlias);
	}

	/**
	 * Verify the signature of the xml string
	 * 
	 * @param xmlString
	 *            XML string whose signature to be verified
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(java.lang.String xmlString) throws Exception {
		return verifyXMLSignature(xmlString, null);
	}

	/**
	 * Verify the signature of the xml string
	 * 
	 * @param xmlString
	 *            XML string whose signature to be verified
	 * @param certAlias
	 *            certAlias alias for Signer's certificate, this is used to
	 *            search signer's public certificate if it is not presented in
	 *            ds:KeyInfo
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(java.lang.String xmlString, java.lang.String certAlias) throws Exception {
		return verifyXMLSignature(xmlString, DEF_ID_ATTRIBUTE, certAlias);
	}

	/**
	 * Verify the signature of the xml string
	 * 
	 * @param xmlString
	 *            XML string whose signature to be verified
	 * @param idAttrName
	 *            Attribute name for the id attribute
	 * @param certAlias
	 *            certAlias alias for Signer's certificate, this is used to
	 *            search signer's public certificate if it is not presented in
	 *            ds:KeyInfo
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(java.lang.String xmlString, java.lang.String idAttrName,
			java.lang.String certAlias) throws Exception {
		if (xmlString == null || xmlString.length() == 0) {
			throw new Exception("signXML: xmlString is null.");
		}

		Document doc = XMLUtils.toDOMDocument(xmlString);
		try {
			return verifyXMLSignature(doc, idAttrName, certAlias);
		} catch (Exception ex) {
			throw new Exception("verifyXMLSignature Exception: " + ex.getMessage());
		}
	}

	/**
	 * Verify the signature of a DOM Document
	 * 
	 * @param doc
	 *            a DOM Document
	 * @param idAttrName
	 *            Attribute name for the id attribute
	 * @param certAlias
	 *            certAlias alias for Signer's certificate, this is used to
	 *            search signer's public certificate if it is not presented in
	 *            ds:KeyInfo
	 * @return true if the xml signature is verified, false otherwise
	 * @throws Exception
	 *             if problem occurs during verification
	 */
	public boolean verifyXMLSignature(Document doc, java.lang.String idAttrName, java.lang.String certAlias)
			throws Exception {
		try {
			Element nscontext = XMLUtils.createDSctx(doc, "ds", Constants.SignatureSpecNS);
			Element sigElement = (Element) XPathAPI.selectSingleNode(doc, "//ds:Signature[1]", nscontext);
			XMLSignature signature = new XMLSignature(sigElement, "");
			// comment the below code, need future clean-up
			/*
			 * if (!idAttrName.equals(DEF_ID_ATTRIBUTE)) { // TODO : this only
			 * work when doc is the element which // reference URI pointed to.
			 * Need more work, possible // solution is have a customized
			 * ResouceResolver Element root = doc.getDocumentElement(); String
			 * id = root.getAttribute(idAttrName); // register the id for the
			 * elment, so it could be found // by Reference object based on URI
			 * if (id != null && !id.length() == 0) {
			 * IdResolver.registerElementById(root, id); } }
			 */
			String idValue = doc.getDocumentElement().getAttribute(idAttrName);
			Element root = (Element) XPathAPI.selectSingleNode(doc, "//*[@" + idAttrName + "=\"" + idValue + "\"]");

			if (root == null) {
				throw new Exception("verifyXML: could not resolv id attribute");
			}
			String[] attrs = { "AssertionID", "RequestID", "ResponseID" };
			for (int j = 0; j < attrs.length; j++) {
				NodeList aList = (NodeList) XPathAPI.selectNodeList(doc, "//*[@" + attrs[j] + "]");
				if (aList != null && aList.getLength() != 0) {
					int len = aList.getLength();

					for (int i = 0; i < len; i++) {
						Element elem = (Element) aList.item(i);
						String id = elem.getAttribute(attrs[j]);
						if (id != null && id.length() != 0) {
							IdResolver.registerElementById(elem, id);
						}
					}
				}
			}
			KeyInfo ki = signature.getKeyInfo();
			PublicKey pk = this.getX509PublicKey(doc, ki);
			if (pk != null) {
				// verify using public key
				if (signature.checkSignatureValue(pk)) {
					return true;
				} else {
					return false;
				}
			} else {
				if (certAlias == null || certAlias.length() == 0) {
					return false;
				}
				X509Certificate newcert = (X509Certificate) keystore.getCertificate(certAlias);
				if (newcert != null) {
					if (signature.checkSignatureValue(newcert)) {
						return true;
					} else {
						return false;
					}
				} else {
					PublicKey key = keystore.getPublicKey(certAlias);
					if (key != null) {
						if (signature.checkSignatureValue(key)) {
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		} catch (Exception ex) {
			throw new Exception("verifyXMLSignature Exception: " + ex.getMessage());
		}
	}

	/**
	 * Get the real key provider
	 * 
	 * @return KeyProvider
	 */
	public KeyProvider getKeyProvider() {
		return keystore;
	}

	/**
	 * Get the X509Certificate embedded in the KeyInfo
	 * 
	 * @param keyinfo
	 *            KeyInfo
	 * @return a X509Certificate
	 */
	protected PublicKey getX509PublicKey(Document doc, KeyInfo keyinfo) {
		PublicKey pk = null;
		try {
			if (keyinfo != null) {
				if (isJKSKeyStore) {
					StorageResolver storageResolver = new StorageResolver(new KeyStoreResolver(keystore.getKeyStore()));
					keyinfo.addStorageResolver(storageResolver);
					keyinfo.registerInternalKeyResolver(new X509IssuerSerialResolver());
					keyinfo.registerInternalKeyResolver(new X509CertificateResolver());
					keyinfo.registerInternalKeyResolver(new X509SKIResolver());
					keyinfo.registerInternalKeyResolver(new X509SubjectNameResolver());
				}
				if (keyinfo.containsX509Data()) {
					X509Certificate certificate = keyinfo.getX509Certificate();
					// use a systemproperty identity.saml.checkcert
					// defined in AMConfig.properties, as a nob to check the
					// the validity of the cert.
					pk = getPublicKey(certificate);
				} else {
					// Do we need to check if the public key is in the
					// keystore!?
					pk = getWSSTokenProfilePublicKey(doc);
				}
			}
		} catch (Exception e) {
			System.out.println("getX509Certificate(KeyInfo) Exception: " + e.getMessage());
		}

		return pk;
	}

	/**
	 * Get the PublicKey embedded in the Security Token profile
	 * 
	 * @param Document
	 *            the docement to be verified
	 * @return a PublicKey
	 */
	private PublicKey getWSSTokenProfilePublicKey(Document doc) {
		PublicKey pubKey = null;

		try {
			String wsseNS = SAMLConstants.NS_WSSE;
			String wsuNS = SAMLConstants.NS_WSU;
			if ((wsfVersion != null) && (wsfVersion.equals(SOAPBindingConstants.WSF_11_VERSION))) {
				wsseNS = WSSEConstants.NS_WSSE_WSF11;
				wsuNS = WSSEConstants.NS_WSU_WSF11;
			}
			Element securityElement = (Element) doc.getDocumentElement()
					.getElementsByTagNameNS(wsseNS, SAMLConstants.TAG_SECURITY).item(0);
			if (securityElement == null) {
				return null;
			}

			Element nscontext = XMLUtils.createDSctx(doc, "ds", Constants.SignatureSpecNS);
			Element sigElement = (Element) XPathAPI.selectSingleNode(securityElement, "ds:Signature[1]", nscontext);

			Element keyinfo = (Element) sigElement
					.getElementsByTagNameNS(Constants.SignatureSpecNS, SAMLConstants.TAG_KEYINFO).item(0);
			Element str = (Element) keyinfo.getElementsByTagNameNS(wsseNS, SAMLConstants.TAG_SECURITYTOKENREFERENCE)
					.item(0);
			Element reference = (Element) keyinfo.getElementsByTagNameNS(wsseNS, SAMLConstants.TAG_REFERENCE).item(0);

			if (reference != null) {
				String id = reference.getAttribute(SAMLConstants.TAG_URI);
				id = id.substring(1);
				nscontext = XMLUtils.createDSctx(doc, SAMLConstants.PREFIX_WSU, wsuNS);
				Node n = XPathAPI.selectSingleNode(doc,
						"//*[@" + SAMLConstants.PREFIX_WSU + ":" + SAMLConstants.TAG_ID + "=\"" + id + "\"]",
						nscontext);

				if (n != null) { // X509 Security Token profile
					String format = ((Element) n).getAttribute(SAMLConstants.TAG_VALUETYPE);
					NodeList children = n.getChildNodes();
					n = children.item(0);
					String certString = n.getNodeValue().trim();

					pubKey = getPublicKey(getCertificate(certString, format));

				} else { // SAML Token profile
					reference = (Element) XPathAPI.selectSingleNode(doc, "//*[@AssertionID=\"" + id + "\"]");
					// The SAML Statements contain keyinfo, they should be
					// all the same. get the first keyinfo!
					reference = (Element) reference
							.getElementsByTagNameNS(Constants.SignatureSpecNS, SAMLConstants.TAG_KEYINFO).item(0);
					if (reference == null) { // no cert found!
						throw new Exception("getWSSTokenProfilePublicKey: no KeyInfo found!");
					}
					Element x509Data = (Element) reference
							.getElementsByTagNameNS(Constants.SignatureSpecNS, SAMLConstants.TAG_X509DATA).item(0);
					if (x509Data != null) { // Keyinfo constains certificate
						reference = (Element) x509Data.getChildNodes().item(0);
						String certString = x509Data.getChildNodes().item(0).getChildNodes().item(0).getNodeValue();
						return getPublicKey(getCertificate(certString, null));
					} else { // it should contains RSA/DSA key
						pubKey = getPublicKeybyDSARSAkeyValue(doc, reference);
					}
				}
			}
		} catch (Exception e) {
			System.out.println("getWSSTokenProfilePublicKey Exception: " + e.getMessage());
		}
		return pubKey;
	}

	protected PublicKey getPublicKeybyDSARSAkeyValue(Document doc, Element reference) throws Exception {

		PublicKey pubKey = null;
		Element dsaKey = (Element) reference
				.getElementsByTagNameNS(Constants.SignatureSpecNS, SAMLConstants.TAG_DSAKEYVALUE).item(0);
		if (dsaKey != null) { // It's DSAKey
			NodeList nodes = dsaKey.getChildNodes();
			int nodeCount = nodes.getLength();
			if (nodeCount > 0) {
				BigInteger p = null, q = null, g = null, y = null;
				for (int i = 0; i < nodeCount; i++) {
					Node currentNode = nodes.item(i);
					if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
						String tagName = currentNode.getLocalName();
						Node sub = currentNode.getChildNodes().item(0);
						String value = sub.getNodeValue();
						BigInteger v = new BigInteger(Base64.decode(value));
						if (tagName.equals("P")) {
							p = v;
						} else if (tagName.equals("Q")) {
							q = v;
						} else if (tagName.equals("G")) {
							g = v;
						} else if (tagName.equals("Y")) {
							y = v;
						} else {
							throw new Exception("error Obtain PK");
						}
					}
				}
				DSAKeyValue dsaKeyValue = new DSAKeyValue(doc, p, q, g, y);
				try {
					pubKey = dsaKeyValue.getPublicKey();
				} catch (Exception e) {
					throw new Exception("error Obtain PK");
				}
			}
		} else {
			Element rsaKey = (Element) reference
					.getElementsByTagNameNS(Constants.SignatureSpecNS, SAMLConstants.TAG_RSAKEYVALUE).item(0);
			if (rsaKey != null) { // It's RSAKey
				NodeList nodes = rsaKey.getChildNodes();
				int nodeCount = nodes.getLength();
				BigInteger m = null, e = null;
				if (nodeCount > 0) {
					for (int i = 0; i < nodeCount; i++) {
						Node currentNode = nodes.item(i);
						if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
							String tagName = currentNode.getLocalName();
							Node sub = currentNode.getChildNodes().item(0);
							String value = sub.getNodeValue();
							BigInteger v = new BigInteger(Base64.decode(value));
							if (tagName.equals("Exponent")) {
								e = v;
							} else if (tagName.equals("Modulus")) {
								m = v;
							} else {
								throw new Exception("error Obtain PK");
							}
						}
					}
				}
				RSAKeyValue rsaKeyValue = new RSAKeyValue(doc, m, e);
				try {
					pubKey = rsaKeyValue.getPublicKey();
				} catch (Exception ex) {
					throw new Exception("error Obtain PK");
				}
			}
		}
		return pubKey;
	}

	/**
	 * Get the X509Certificate from encoded cert string
	 * 
	 * @param certString
	 *            BASE64 or PKCS7 encoded certtificate string
	 * @param format
	 *            encoded format
	 * @return a X509Certificate
	 */
	protected X509Certificate getCertificate(String certString, String format) {
		X509Certificate cert = null;

		try {

			StringBuffer xml = new StringBuffer(100);
			xml.append(SAMLConstants.BEGIN_CERT);
			xml.append(certString);
			xml.append(SAMLConstants.END_CERT);

			byte[] barr = null;
			barr = (xml.toString()).getBytes();

			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			ByteArrayInputStream bais = new ByteArrayInputStream(barr);

			if ((format != null) && format.equals(SAMLConstants.TAG_PKCS7)) { // PKCS7
																				// format
				Collection c = cf.generateCertificates(bais);
				Iterator i = c.iterator();
				while (i.hasNext()) {
					cert = (java.security.cert.X509Certificate) i.next();
				}
			} else { // X509:v3 format
				while (bais.available() > 0) {
					cert = (java.security.cert.X509Certificate) cf.generateCertificate(bais);
				}
			}
		} catch (Exception e) {
			System.out.println("getCertificate Exception: " + e.getMessage());
		}

		return cert;
	}

	/**
	 * Returns the public key from the certificate embedded in the KeyInfo.
	 *
	 * @param cert
	 *            X509 Certificate
	 * @return a public key from the certificate embedded in the KeyInfo.
	 */
	protected PublicKey getPublicKey(X509Certificate cert) {
		PublicKey pk = null;
		if (cert != null) {
			pk = cert.getPublicKey();
		}
		return pk;
	}

	protected boolean isValidAlgorithm(String algorithm) {
		if (algorithm.equals(SAMLConstants.ALGO_ID_MAC_HMAC_SHA1)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_DSA)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_RSA)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_RSA_SHA1)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_RSA_RIPEMD160)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_RSA_SHA256)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_RSA_SHA384)
				|| algorithm.equals(SAMLConstants.ALGO_ID_SIGNATURE_RSA_SHA512)
				|| algorithm.equals(SAMLConstants.ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5)
				|| algorithm.equals(SAMLConstants.ALGO_ID_MAC_HMAC_RIPEMD160)
				|| algorithm.equals(SAMLConstants.ALGO_ID_MAC_HMAC_SHA256)
				|| algorithm.equals(SAMLConstants.ALGO_ID_MAC_HMAC_SHA384)
				|| algorithm.equals(SAMLConstants.ALGO_ID_MAC_HMAC_SHA512)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidCanonicalizationMethod(String algorithm) {
		if (algorithm.equals(SAMLConstants.ALGO_ID_C14N_OMIT_COMMENTS)
				|| algorithm.equals(SAMLConstants.ALGO_ID_C14N_WITH_COMMENTS)
				|| algorithm.equals(SAMLConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS)
				|| algorithm.equals(SAMLConstants.ALGO_ID_C14N_EXCL_WITH_COMMENTS)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isValidTransformAlgorithm(String algorithm) {
		if (algorithm.equals(SAMLConstants.TRANSFORM_C14N_OMIT_COMMENTS)
				|| algorithm.equals(SAMLConstants.TRANSFORM_C14N_WITH_COMMENTS)
				|| algorithm.equals(SAMLConstants.TRANSFORM_C14N_EXCL_OMIT_COMMENTS)
				|| algorithm.equals(SAMLConstants.TRANSFORM_C14N_EXCL_WITH_COMMENTS)
				|| algorithm.equals(SAMLConstants.TRANSFORM_XSLT)
				|| algorithm.equals(SAMLConstants.TRANSFORM_BASE64_DECODE)
				|| algorithm.equals(SAMLConstants.TRANSFORM_XPATH)
				|| algorithm.equals(SAMLConstants.TRANSFORM_ENVELOPED_SIGNATURE)
				|| algorithm.equals(SAMLConstants.TRANSFORM_XPOINTER)
				|| algorithm.equals(SAMLConstants.TRANSFORM_XPATH2FILTER04)
				|| algorithm.equals(SAMLConstants.TRANSFORM_XPATH2FILTER)
				|| algorithm.equals(SAMLConstants.TRANSFORM_XPATHFILTERCHGP)) {
			return true;
		} else {
			return false;
		}
	}

	private String getKeyAlgorithm(PrivateKey pk) {
		if (defaultSigAlg != null && !defaultSigAlg.equals("")) {
			return defaultSigAlg;
		}
		if (pk.getAlgorithm().equalsIgnoreCase("DSA")) {
			return SAMLConstants.ALGO_ID_SIGNATURE_DSA;
		}
		// return SAMLConstants.ALGO_ID_SIGNATURE_RSA_SHA1;
		return SAMLConstants.ALGO_ID_SIGNATURE_RSA_SHA256;
	}

	/**
	 * Return algorithm URI for the given algorithm.
	 */
	protected String getAlgorithmURI(String algorithm) {
		if (algorithm == null) {
			return null;
		}
		if (algorithm.equals("RSA")) {
			return SAMLConstants.ALGO_ID_SIGNATURE_RSA;
		} else if (algorithm.equals("DSA")) {
			return SAMLConstants.ALGO_ID_SIGNATURE_DSA;
		} else {
			return null;
		}
	}
}
