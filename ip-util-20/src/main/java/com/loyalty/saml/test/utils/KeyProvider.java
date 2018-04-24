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
 * $Id: KeyProvider.java,v 1.5 2009/07/02 21:53:26 madan_ranganath Exp $
 *
 */

package com.loyalty.saml.test.utils;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.lang.*;

/**
 * The class <code>KeyProvider</code> is an interface that is implemented to
 * retrieve X509Certificates and Private Keys from user data store.
 * <p>
 *
 * @supported.all.api
 */

public interface KeyProvider {

	/**
	 * Return <code>java.security.cert.X509Certificate</code> for the specified
	 * <code>certAlias</code>.
	 * 
	 * @param certAlias
	 *            Certificate alias name
	 * @return <code>X509Certificate</code> which matches the
	 *         <code>certAlias</code>, return null if the certificate could not
	 *         be found.
	 */
	public Certificate getCertificate(String certAlias);

	/**
	 * Returns <code>java.security.PublicKey</code> for the specified
	 * <code>keyAlias</code>
	 *
	 * @param keyAlias
	 *            Key alias name
	 * @return <code>PublicKey</code> which matches the <code>keyAlias</code>,
	 *         return null if the <code>PublicKey</code> could not be found.
	 */
	public java.security.PublicKey getPublicKey(String keyAlias);

	/**
	 * Returns <code>java.security.PrivateKey</code> for the specified
	 * <code>certAlias</code>.
	 *
	 * @param certAlias
	 *            Certificate alias name
	 * @return <code>PrivateKey</code> which matches the <code>certAlias</code>,
	 *         return null if the private key could not be found.
	 */
	public java.security.PrivateKey getPrivateKey(String certAlias);

	/**
	 * Returns the keystore instance.
	 * 
	 * @return the keystore instance.
	 */
	public KeyStore getKeyStore();
}
