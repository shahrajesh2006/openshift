package com.loyalty.common.ilclient;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import com.loyalty.web.application.ApplicationContextProvider;
import com.loyalty.web.utils.IAgentSSOAuthUtils;
import com.loyalty.web.vo.IAgentSSOPayloadVO;

/**
 * This program encrypts the parameters, generates the xml document, and passes
 * the encrypted token to the integrated login page to submit to the app.
 */

public class IAgentSSOEncryptBean {
	private String agentUserName = "";
	private String marketSegmentOne = "";
	private String accountNumber = "";
	private String sourceID = "";
	private String vendorID = "";
	private String clientProgramCode = "";
	private String jSecurityCheckURL = "";
	private String bypassClientService = "";

	final static Logger log = LoggerFactory.getLogger(IAgentSSOEncryptBean.class);
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public void process() throws IOException {
		if (request.getMethod().equals(RequestMethod.GET.toString())) {
			log.info("Get request ignored");
			return;
		}
		try {
			IAgentSSOAuthUtils IAgentSSOAuthUtils = ApplicationContextProvider.getContext().getBean(IAgentSSOAuthUtils.class);
			IAgentSSOPayloadVO iAgentSSOPayloadVO = new IAgentSSOPayloadVO();
			iAgentSSOPayloadVO.setAccountNumber(getAccountNumber());
			iAgentSSOPayloadVO.setAgentUserName(getAgentUserName());
			iAgentSSOPayloadVO.setMarketSegmentOne(getMarketSegmentOne());
			iAgentSSOPayloadVO.setSourceID(getSourceID());
			iAgentSSOPayloadVO.setVendorID(getVendorID());
			iAgentSSOPayloadVO.setBypassClientService(getBypassClientService());

			String rawXml = IAgentSSOAuthUtils.serializePayload(iAgentSSOPayloadVO);

			// Encrypt and convert to hex array
			String clientProgramCode = getClientProgramCode();
			String encryptedXml = IAgentSSOAuthUtils.encrypt(rawXml, clientProgramCode);

			// we have to use these "temp" request parms because weblogic
			// snatches j_username, and j_password from the request
			// no matter where the post is directed
			request.setAttribute(IntegratedLoginConstants.TEMP_J_USERNAME_PARAMETER_NAME, encryptedXml);
			request.setAttribute(IntegratedLoginConstants.J_SECURITY_CHECK_PARAMETER_NAME, getjSecurityCheckURL());

			// redirect to intermediate page
			this.forward("TransferTo_IAgent.jsp");
		} catch (Exception e) {

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

	public String getAgentUserName() {
		return agentUserName;
	}

	public void setAgentUserName(String agentUserName) {
		this.agentUserName = agentUserName;
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

	public String getClientProgramCode() {
		return clientProgramCode;
	}

	public void setClientProgramCode(String clientProgramCode) {
		this.clientProgramCode = clientProgramCode;
	}

	public String getjSecurityCheckURL() {
		return jSecurityCheckURL;
	}

	public void setjSecurityCheckURL(String jSecurityCheckURL) {
		this.jSecurityCheckURL = jSecurityCheckURL;
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
}
