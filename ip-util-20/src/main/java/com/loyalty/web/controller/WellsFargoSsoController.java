package com.loyalty.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.loyalty.saml.test.utils.SamlTestHelper;

/**
 * @author rgagliardo
 */
@Controller
public class WellsFargoSsoController
{
	@RequestMapping(value="/WellsFargoSsoAction",method = RequestMethod.POST)
    public String postValues(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        
        // Security Check
        String environment = getEnvironment(request);

        // Do not allow sso jump page testing in prod
        if ("prod".equalsIgnoreCase(environment))
        {
        	return "NotAuthorized";
        }

        String cardNumber = request.getParameter("cardNumber");
        String lastFourSsn = request.getParameter("lastFourSsn");
        String authURL = request.getParameter("authURL");
        String encryptCertAlias = request.getParameter("encryptCertAlias");
        String fraudInterdiction = request.getParameter("fraudInterdiction");
        String token = SamlTestHelper.createSamlToken(cardNumber, lastFourSsn, authURL, encryptCertAlias, fraudInterdiction);
        String ssoUrl = request.getParameter("authURL");
        request.setAttribute("samlToken", token);
        request.setAttribute("ssoUrl", ssoUrl);
        return "WellsFargoSamlSubmit";

    }

    String getEnvironment(HttpServletRequest request) throws ServletException
    {
        if (request.getServerName().equals("localhost"))
        {
            return "local";
        }

        return System.getProperty("cxl.environment");
    }


}