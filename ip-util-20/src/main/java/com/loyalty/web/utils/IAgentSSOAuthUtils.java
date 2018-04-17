package com.loyalty.web.utils;

import java.io.StringWriter;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loyalty.web.data.dao.KeystoreDAO;
import com.loyalty.web.vo.IAgentSSOPayloadVO;

@Service
public class IAgentSSOAuthUtils {
	
	@Autowired
	KeystoreDAO dao;
	
	final static Logger log = LoggerFactory.getLogger(IAgentSSOAuthUtils.class);

	// Serialize a payload into an xml document. Used by ip-util-20 to generate
	// xml to simulate SSO from Discover
	public String serializePayload(IAgentSSOPayloadVO iAgentSSOPayloadVO) throws Exception {

		Document document = DocumentHelper.createDocument();

		Element payload = document.addElement("payload");
		// Mandatory fields from Orion to iAgent
		payload.addElement("timestamp").setText(AuthUtils.generateTimestamp());
		if (StringUtils.isNotBlank(iAgentSSOPayloadVO.getMarketSegmentOne())) {
			payload.addElement("marketSegmentOne").setText(iAgentSSOPayloadVO.getMarketSegmentOne());
		}
		if (StringUtils.isNotBlank(iAgentSSOPayloadVO.getAccountNumber())) {
			payload.addElement("accountKey").setText(iAgentSSOPayloadVO.getAccountNumber());
		}
		payload.addElement("agentUserName").setText(iAgentSSOPayloadVO.getAgentUserName());
		payload.addElement("sourceID").setText(iAgentSSOPayloadVO.getSourceID());
		payload.addElement("vendorID").setText(iAgentSSOPayloadVO.getVendorID());
		if (StringUtils.isNotBlank(iAgentSSOPayloadVO.getBypassClientService())) {
			payload.addElement("bypassClientService").setText(iAgentSSOPayloadVO.getBypassClientService());
		}

		StringWriter stringWriter = new StringWriter();
		XMLWriter writer = new XMLWriter(stringWriter, OutputFormat.createPrettyPrint());
		writer.write(document);
		return stringWriter.toString();
	}

	public static IAgentSSOPayloadVO deserializePayload(String payload) throws Exception {
		log.debug("IAgentSSOPayloadVO:" + payload);
		try {
			Document document = DocumentHelper.parseText(payload);

			IAgentSSOPayloadVO iAgentSSOPayloadVO = new IAgentSSOPayloadVO();
			// In case element is not sent at all (instead of being sent but
			// with an empty value), perform null check to prevent nullpointer
			if (document.selectSingleNode("//accountKey") != null)
				iAgentSSOPayloadVO.setAccountNumber(document.selectSingleNode("//accountKey").getText());
			if (document.selectSingleNode("//accountId") != null)
				iAgentSSOPayloadVO.setAccountId(document.selectSingleNode("//accountId").getText());
			if (document.selectSingleNode("//agentUserName") != null)
				iAgentSSOPayloadVO.setAgentUserName(document.selectSingleNode("//agentUserName").getText());
			if (document.selectSingleNode("//marketSegmentOne") != null)
				iAgentSSOPayloadVO.setMarketSegmentOne(document.selectSingleNode("//marketSegmentOne").getText());
			if (document.selectSingleNode("//sourceID") != null)
				iAgentSSOPayloadVO.setSourceID(document.selectSingleNode("//sourceID").getText());
			if (document.selectSingleNode("//vendorID") != null)
				iAgentSSOPayloadVO.setVendorID(document.selectSingleNode("//vendorID").getText());
			if (document.selectSingleNode("//bypassClientService") != null)
				iAgentSSOPayloadVO.setBypassClientService(document.selectSingleNode("//bypassClientService").getText());
			if (document.selectSingleNode("//landingPage") != null)
				iAgentSSOPayloadVO.setLandingPage(document.selectSingleNode("//landingPage").getText());
			if (document.selectSingleNode("//clientId") != null)
				iAgentSSOPayloadVO.getClientInfo()
						.setClientID(new Long(document.selectSingleNode("//clientId").getText()));
			if (document.selectSingleNode("//clientName") != null)
				iAgentSSOPayloadVO.getClientInfo().setClientName(document.selectSingleNode("//clientName").getText());
			if (document.selectSingleNode("//clientProgramId") != null)
				iAgentSSOPayloadVO.getClientInfo()
						.setClientProgramID(new Long(document.selectSingleNode("//clientProgramId").getText()));
			if (document.selectSingleNode("//clientProgramCode") != null)
				iAgentSSOPayloadVO.getClientInfo()
						.setClientProgramCode(document.selectSingleNode("//clientProgramCode").getText());
			if (document.selectSingleNode("//clientProgramName") != null)
				iAgentSSOPayloadVO.getClientInfo()
						.setClientProgramName(document.selectSingleNode("//clientProgramName").getText());
			if (document.selectSingleNode("//portfolioId") != null)
				iAgentSSOPayloadVO.getClientInfo()
						.setPortfolioID(new Long(document.selectSingleNode("//portfolioId").getText()));
			if (document.selectSingleNode("//portfolioCode") != null)
				iAgentSSOPayloadVO.getClientInfo()
						.setPortfolioCode(document.selectSingleNode("//portfolioCode").getText());
			if (document.selectSingleNode("//portfolioName") != null)
				iAgentSSOPayloadVO.getClientInfo()
						.setPortfolioName(document.selectSingleNode("//portfolioName").getText());

			return iAgentSSOPayloadVO;
		} catch (DocumentException x) {
			String msg = "Problem deserializing XML payload for iAgent.";

			log.error(msg, x);

			throw (x);
		}
	}

	/**
	 * Using the clientprogram/iagent keyset encrypt the <code>payload</code>.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String text, String clientProgramCode) throws Exception {
		log.debug("Payload In Clear Text:" + text);
		String encryptedString;
		Vector keyVector = dao.getClientPartnerProgramKeystore(clientProgramCode, "IG", "I");
		byte[] key = Conversion.hexStringToByteArray((String) keyVector.get(0));
		byte[] iv = Conversion.hexStringToByteArray((String) keyVector.get(3));

		encryptedString = Conversion.byteToHex(AESHelper.getInstance().encrypt(key, iv, text));
		return encryptedString;
	}

	/**
	 * Using the clientprogram/iagent keyset decrypt the <code>payload</code>.
	 * 
	 * @param payload
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String text, String clientProgramCode) throws Exception {
		String decryptedString;
		Vector keyVector = dao.getClientPartnerProgramKeystore(clientProgramCode, "IG", "I");
		byte[] key = Conversion.hexStringToByteArray((String) keyVector.get(0));
		byte[] iv = Conversion.hexStringToByteArray((String) keyVector.get(3));

		decryptedString = AESHelper.getInstance().decrypt(key, iv, Conversion.hexStringToByteArray(text));
		log.debug("Payload In Clear Text:" + decryptedString);
		return decryptedString;
	}

}
