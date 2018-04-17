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
import com.loyalty.web.errors.SystemException;
import com.loyalty.web.vo.OMASSOPayloadVO;

@Service
public class OMASSOAuthUtils
{
	final static Logger log = LoggerFactory.getLogger(OMASSOAuthUtils.class);

	@Autowired
	KeystoreDAO dao;
	
    //Serialize a payload into an xml document.  Used by ip-util-20 to generate xml to simulate SSO from Discover
    public String serializePayload(OMASSOPayloadVO omaSSOPayloadVO) throws Exception
    {

        Document document = DocumentHelper.createDocument();

        Element payload = document.addElement("payload");
        // Mandatory fields from Discover Online to OMA
        payload.addElement("timestamp").setText(AuthUtils.generateTimestamp());
        payload.addElement("marketSegmentOne").setText(omaSSOPayloadVO.getMarketSegmentOne());
        payload.addElement("accountKey").setText(omaSSOPayloadVO.getAccountNumber());
        //Null check optional fields, because Discover may not send the element at all so we don't want to blow up.
        //SourceID, vendorID are never sent from them for OMA, but left in here so we easily support them if they change their mind
        if (StringUtils.isNotBlank(omaSSOPayloadVO.getSourceID()))
        {
            payload.addElement("sourceID").setText(omaSSOPayloadVO.getSourceID());
        }
        if (StringUtils.isNotBlank(omaSSOPayloadVO.getVendorID()))
        {
            payload.addElement("vendorID").setText(omaSSOPayloadVO.getVendorID());
        }
        if (StringUtils.isNotBlank(omaSSOPayloadVO.getOfferCode()))
        {
            payload.addElement("offerCode").setText(omaSSOPayloadVO.getOfferCode());
        }
        //Added cateCode for Discover Exclusives
        if (StringUtils.isNotBlank(omaSSOPayloadVO.getCatCode()))
        {
            payload.addElement("catCode").setText(omaSSOPayloadVO.getCatCode());
        }        
        
        //Added cateCode for Discover Twitter
        if (StringUtils.isNotBlank(omaSSOPayloadVO.getExternalSource()))
        {
            payload.addElement("externalSource").setText(omaSSOPayloadVO.getExternalSource());
        }
        payload.addElement("sessionID").setText(omaSSOPayloadVO.getSessionID());
        payload.addElement("authURL").setText(omaSSOPayloadVO.getAuthURL());
        payload.addElement("pingURL").setText(omaSSOPayloadVO.getPingURL());
        payload.addElement("logoutURL").setText(omaSSOPayloadVO.getLogoutURL());
        payload.addElement("siteID").setText(omaSSOPayloadVO.getSiteID());        
        if (StringUtils.isNotBlank(omaSSOPayloadVO.getBypassClientService()))
        {
            payload.addElement("bypassClientService").setText(omaSSOPayloadVO.getBypassClientService());
        }

        StringWriter stringWriter = new StringWriter();
        XMLWriter writer = new XMLWriter(stringWriter, OutputFormat.createPrettyPrint());
        writer.write(document);
        return stringWriter.toString();
    }

    public static OMASSOPayloadVO deserializePayload(String payload) throws SystemException
    {
        log.debug("OMASSOPayloadVO:" + payload);
        try
        {
            Document document = DocumentHelper.parseText(payload);

            OMASSOPayloadVO omaSSOPayloadVO = new OMASSOPayloadVO();
            //Null check fields, because Discover may not send the element at all so we don't want to blow up.
            //SourceID, vendorID are never sent from them for OMA, but left in here so we easily support them if they change their mind
            if (document.selectSingleNode("//accountKey") != null)
            {
                omaSSOPayloadVO.setAccountNumber(document.selectSingleNode("//accountKey").getText());
            }
            if (document.selectSingleNode("//marketSegmentOne") != null)
            {
                omaSSOPayloadVO.setMarketSegmentOne(document.selectSingleNode("//marketSegmentOne").getText());
            }
            if (document.selectSingleNode("//sourceID") != null)
            {
                omaSSOPayloadVO.setSourceID(document.selectSingleNode("//sourceID").getText());
            }
            if (document.selectSingleNode("//vendorID") != null)
            {
                omaSSOPayloadVO.setVendorID(document.selectSingleNode("//vendorID").getText());
            }
            if (document.selectSingleNode("//sessionID") != null)
            {
                omaSSOPayloadVO.setSessionID(document.selectSingleNode("//sessionID").getText());
            }
            //Used for deep linking to an offer
            if (document.selectSingleNode("//offerCode") != null)
            {
                omaSSOPayloadVO.setOfferCode(document.selectSingleNode("//offerCode").getText());
            }
            //Added cateCode for Discover Exclusives
            if (document.selectSingleNode("//catCode") != null)
            {
                omaSSOPayloadVO.setCatCode(document.selectSingleNode("//catCode").getText());
            }            
            //Added cateCode for Discover Twitter
            if (document.selectSingleNode("//externalSource") != null)
            {
                omaSSOPayloadVO.setExternalSource(document.selectSingleNode("//externalSource").getText());
            }             
            if (document.selectSingleNode("//authURL") != null)
            {
                omaSSOPayloadVO.setAuthURL(document.selectSingleNode("//authURL").getText());
            }
            if (document.selectSingleNode("//pingURL") != null)
            {
                omaSSOPayloadVO.setPingURL(document.selectSingleNode("//pingURL").getText());
            }
            if (document.selectSingleNode("//logoutURL") != null)
            {
                omaSSOPayloadVO.setLogoutURL(document.selectSingleNode("//logoutURL").getText());
            }
            if (document.selectSingleNode("//siteID") != null)
            {
                omaSSOPayloadVO.setSiteID(document.selectSingleNode("//siteID").getText());
            }
            if (document.selectSingleNode("//bypassClientService") != null)
            {
                omaSSOPayloadVO.setBypassClientService(document.selectSingleNode("//bypassClientService").getText());
            }

            return omaSSOPayloadVO;
        }
        catch (DocumentException x)
        {
            String msg = "Problem deserializing XML payload for OMA.";

            log.error(msg, x);

            throw new SystemException(null, msg, x);
        }
    }

    /**
     * Using the clientprogram/iagent keyset encrypt the <code>payload</code>.
     * 
     * @param payload
     * @return
     * @throws SystemException
     */
    public String encrypt(String text, int clientProgramId) throws Exception
    {
        log.debug("Payload In Clear Text:" + text);
        String encryptedString;
        Vector keyVector = dao.getClientProgramRawKeystore(clientProgramId);
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
     * @throws SystemException
     */
    public String decrypt(String text, int clientProgramId) throws Exception
    {
        String decryptedString;
        Vector keyVector = dao.getClientProgramRawKeystore(clientProgramId);
        byte[] key = Conversion.hexStringToByteArray((String) keyVector.get(0));
        byte[] iv = Conversion.hexStringToByteArray((String) keyVector.get(3));

        decryptedString = AESHelper.getInstance().decrypt(key, iv, Conversion.hexStringToByteArray(text));
        return decryptedString;
    }

}
