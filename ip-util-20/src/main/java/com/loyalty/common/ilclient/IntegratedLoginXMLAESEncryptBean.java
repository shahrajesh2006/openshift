package com.loyalty.common.ilclient;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;

import com.loyalty.web.application.ApplicationContextProvider;
import com.loyalty.web.utils.OMASSOAuthUtils;
import com.loyalty.web.vo.OMASSOPayloadVO;

/**
 * This program encrypts the parameters, generates the xml document, and passes
 * the encrypted token to the integrated login page to submit to the app.
 */

public class IntegratedLoginXMLAESEncryptBean {
	private String webAppName = "";
	private String marketSegmentOne = "";
	private String accountNumber = "";
	private String sourceID = "";
	private String vendorID = "";
	private String clientProgramId = "";
	private String jSecurityCheckURL = "";
	private String sessionId = "";
	private String tokenType = "";
	private String tokenAlgorithm = "";
	private String tokenIdentifierElement = "";
	private String authURL = "";
	private String pingURL = "";
	private String logoutURL = "";
	private String offerCode = "";
	private String siteID = "";
	private String bypassClientService = "";
	private String catCode = "";

	final static Logger log = LoggerFactory.getLogger(IntegratedLoginXMLAESEncryptBean.class);
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public void process() throws IOException {
		if (request.getMethod().equals(RequestMethod.GET.toString())) {
			log.info("Get request ignored");
			return;
		}
		try {
			OMASSOAuthUtils OMASSOAuthUtils = ApplicationContextProvider.getContext().getBean(OMASSOAuthUtils.class);
			OMASSOPayloadVO omaSSOPayloadVO = new OMASSOPayloadVO();
			omaSSOPayloadVO.setAccountNumber(getAccountNumber());
			omaSSOPayloadVO.setMarketSegmentOne(getMarketSegmentOne());
			omaSSOPayloadVO.setSourceID(getSourceID());
			omaSSOPayloadVO.setVendorID(getVendorID());
			omaSSOPayloadVO.setSessionID(getSessionId());
			omaSSOPayloadVO.setOfferCode(getOfferCode());
			omaSSOPayloadVO.setAuthURL(getAuthURL());
			omaSSOPayloadVO.setPingURL(getPingURL());
			omaSSOPayloadVO.setLogoutURL(getLogoutURL());
			omaSSOPayloadVO.setSiteID(getSiteID());
			omaSSOPayloadVO.setBypassClientService(getBypassClientService());

			// Added cateCode for Discover Exclusives
			omaSSOPayloadVO.setCatCode(getCatCode());

			String rawXml = OMASSOAuthUtils.serializePayload(omaSSOPayloadVO);

			// Encrypt and convert to hex array
			String encryptedXml = OMASSOAuthUtils.encrypt(rawXml, Integer.parseInt(getClientProgramId()));

			// we have to use these "temp" request parms because weblogic
			// snatches j_username, and j_password from the request
			// no matter where the post is directed
			request.setAttribute(IntegratedLoginConstants.TEMP_J_USERNAME_PARAMETER_NAME, encryptedXml);
			request.setAttribute(IntegratedLoginConstants.J_SECURITY_CHECK_PARAMETER_NAME, getjSecurityCheckURL());
			request.setAttribute(IntegratedLoginConstants.TEMP_J_PASSWORD_PARAMETER_NAME, getWebAppName());

			// redirect to intermediate page
			this.forward("TransferTo_XMLIL.jsp");
		} catch (Exception e) {
			log.error(e.getStackTrace().toString());
		}
	}

	/*
	 * Converts char to int
	 */

	protected void redirect(String redirectURL) throws IOException {
		this.response.sendRedirect(redirectURL);
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Wrapper method for the <code>RequestDispatcher</code> forward() method.
	 */
	protected void forward(String forwardResource) throws IOException {
		if (request == null) {
			return;
		} // if
		else if (response.isCommitted()) {
			log.debug(" The response was already commited, your forward to " + forwardResource + " is ignored.");
		} else {
			try {
				// reset the response to remove any previously set headers
				response.reset();
				RequestDispatcher rd = request.getRequestDispatcher(forwardResource);
				if (rd != null) {
					rd.forward(request, response);
				} else {
					// URL not found
					String errMsg = "Could not find requested resource. ";
					log.error(errMsg + forwardResource);
					throw new RuntimeException(errMsg);
				}
			} catch (javax.servlet.ServletException se) {
				String errMsg = "Error occured while processing your request.";
				log.error("ServletException occurred: " + se);
				throw new RuntimeException(errMsg);
			}
		}
	}

	public String getMarketSegmentOne() {
		return marketSegmentOne;
	}

	public void setMarketSegmentOne(String marketSegmentOne) {
		this.marketSegmentOne = marketSegmentOne;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}

	public String getVendorID() {
		return vendorID;
	}

	public void setVendorID(String vendorID) {
		this.vendorID = vendorID;
	}

	public String getjSecurityCheckURL() {
		return jSecurityCheckURL;
	}

	public void setjSecurityCheckURL(String jSecurityCheckURL) {
		this.jSecurityCheckURL = jSecurityCheckURL;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getTokenAlgorithm() {
		return tokenAlgorithm;
	}

	public void setTokenAlgorithm(String tokenAlgorithm) {
		this.tokenAlgorithm = tokenAlgorithm;
	}

	public String getTokenIdentifierElement() {
		return tokenIdentifierElement;
	}

	public void setTokenIdentifierElement(String tokenIdentifierElement) {
		this.tokenIdentifierElement = tokenIdentifierElement;
	}

	public String getClientProgramId() {
		return clientProgramId;
	}

	public void setClientProgramId(String clientProgramId) {
		this.clientProgramId = clientProgramId;
	}

	public String getAuthURL() {
		return authURL;
	}

	public void setAuthURL(String authURL) {
		this.authURL = authURL;
	}

	public String getPingURL() {
		return pingURL;
	}

	public void setPingURL(String pingURL) {
		this.pingURL = pingURL;
	}

	public String getLogoutURL() {
		return logoutURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public String getOfferCode() {
		return offerCode;
	}

	public void setOfferCode(String offerCode) {
		this.offerCode = offerCode;
	}

	public String getWebAppName() {
		return webAppName;
	}

	public void setWebAppName(String webAppName) {
		this.webAppName = webAppName;
	}

	public String getSiteID() {
		return siteID;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	/**
	 * @return the bypassClientService
	 */
	public String getBypassClientService() {
		return bypassClientService;
	}

	/**
	 * @param bypassClientService
	 *            the bypassClientService to set
	 */
	public void setBypassClientService(String bypassClientService) {
		this.bypassClientService = bypassClientService;
	}

	/**
	 * @return category code
	 */
	public String getCatCode() {
		return catCode;
	}

	/**
	 * @param catCode
	 *            category code
	 */
	public void setCatCode(String catCode) {
		this.catCode = catCode;
	}

}
