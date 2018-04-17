package com.loyalty.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.loyalty.saml.test.utils.HealthWellnessSamlTestHelper;

@Controller
public class HealthWellnessSsoController {

	@RequestMapping(value = "/HealthWellnessSsoAction", method = RequestMethod.POST)
	public String postValues(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		boolean passiveMode = false;
		// Security Check
		String environment = getEnvironment(request);
		if ("TRUE".equalsIgnoreCase(System.getProperty("cxl.passive"))) {
			passiveMode = true;
		}

		/*
		 * do not allow the jump pages to work on prod, we are also restricting
		 * this by having invalid certificates on the server for production
		 * (ALG_IP_10.xml.production) so that the payload can't be posted from
		 * other lower environments
		 */

		if ("prod".equalsIgnoreCase(environment) && !passiveMode) {
			return "NotAuthorized";
		}

		// TODO: None of this is actually working because we don't have the
		// private key for signing the saml.
		// See additional notes in ip-util-20.xml
		String attributeStatement = request.getParameter("attributeStatement");
		String publicKeyForEncryptingAssertion = request.getParameter("publicKeyForEncryptingAssertion");
		String privateKeyForSigning = request.getParameter("privateKeyForSigning");
		String authURL = request.getParameter("authURL");
		String issuer = request.getParameter("issuer");
		String nameID = request.getParameter("nameID");
		String token = HealthWellnessSamlTestHelper.createSamlToken(attributeStatement, authURL, issuer,
				privateKeyForSigning, publicKeyForEncryptingAssertion, nameID);
		request.setAttribute("samlToken", token);
		request.setAttribute("ssoUrl", authURL);
		return "HealthWellnessSamlSubmit";

	}

	String getEnvironment(HttpServletRequest request) throws ServletException {
		if (request.getServerName().equals("localhost")) {
			return "local";
		}

		return System.getProperty("cxl.environment");
	}

}
