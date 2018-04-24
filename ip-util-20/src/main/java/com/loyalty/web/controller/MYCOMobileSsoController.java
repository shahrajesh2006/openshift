package com.loyalty.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.loyalty.common.ilclient.DBUtil;
import com.loyalty.saml.test.utils.MycoMobileSamlTestHelper;
import com.loyalty.web.constants.Constants;

@Controller
public class MYCOMobileSsoController
{
 
	@Autowired
	DBUtil DBUtil;
	
	final static Logger log = LoggerFactory.getLogger(MYCOMobileSsoController.class);
	
	@RequestMapping(value="/MYCOMobileSsoAction",method = RequestMethod.POST)
    public String postValues(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
    	log.debug("Inside MYCOMobileSsoController - postValues - start");
  
        // Security Check
        String environment = getEnvironment(request);
        //Commenting to allow prod jump page for QA testing. Uncomment this after launch
        // Do not allow sso jump page testing in prod
        /*if ("prod2".equalsIgnoreCase(environment))
        {
        	return mapping.findForward("doSsoLogin.error");
        }*/

        String accountNumber = request.getParameter("accountNumber"); 
        String authURL = request.getParameter("authURL");
        String encryptCertAlias = request.getParameter("encryptCertAlias");
        String token = MycoMobileSamlTestHelper.createSamlToken(accountNumber, authURL, encryptCertAlias);
        String ssoUrl = request.getParameter("authURL");
        request.setAttribute("samlToken", token);
        request.setAttribute("ssoUrl", ssoUrl);
        
        log.debug("Inside MYCOMobileSsoController - postValues - values == " + accountNumber + " " + authURL + " " + encryptCertAlias + " " + token + " " + ssoUrl);
        
        return "MycoMobileSamlSubmit";

    }

    String getEnvironment(HttpServletRequest request) throws ServletException
    {
        if (request.getServerName().equals("localhost"))
        {
            return "local";
        }

        String sql = "SELECT value env FROM APPLICATION_PROPERTIES " + "WHERE APPLICATION_PROPERTY_CLASS IN "
                + "(SELECT ID FROM APPLICATION_PROPERTY_CLASSES " + "WHERE APPLICATION IN ((SELECT ID FROM APPLICATIONS WHERE NAME = 'Common')) "
                + "AND NAME = 'GenericProperties') " + "AND NAME = 'ENV'";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {
            conn = DBUtil.getConnection(Constants.LOLA_DS);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            rs.next();
            return rs.getString("env");
        }
        catch (Exception x)
        {
            throw new ServletException(x);
        }
        finally
        {
            DBUtil.close(conn, rs, stmt);
        }
    }


}