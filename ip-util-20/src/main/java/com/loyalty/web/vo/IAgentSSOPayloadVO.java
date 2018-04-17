package com.loyalty.web.vo;

import java.io.Serializable;

public class IAgentSSOPayloadVO implements Serializable 
{
    private static final long serialVersionUID = 1L;

    /** User's Unique LOLA Account Logins.primary_login_id in the client's format (actually stored in lola as clientprogramcode_agentUsername)*/
    private String agentUserName;

    /** the account id to be serviced by the agent */ 
    private String accountId; 
     
    /** The account number of the account being serviced */
    private String accountNumber;

    /** Some client identifer that doesn't mean anything to us, but is sent back to them when creating a redemption */
    private String vendorID;

    /** Some client identifer that doesn't mean anything to us, but is sent back to them when creating a redemption */
    private String sourceID;

    /** The segment of the account being serviced.  Never actually used, since the xml callback retrieves and processes this */
    private String marketSegmentOne;
    
    /** True means we don't call the client's service upon initial sign-in, so that we can test access to app without depending on client */
    private String bypassClientService;
    
    /** Client information is needed to display it in the IAgent */
    private ClientInfoVO clientInfo;

    
    private String landingPage;
    
    
    public String getAgentUserName()
    {
        return agentUserName;
    }

    public void setAgentUserName(String agentUserName)
    {
        this.agentUserName = agentUserName;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getVendorID()
    {
        return vendorID;
    }

    public void setVendorID(String vendorID)
    {
        this.vendorID = vendorID;
    }

    public String getSourceID()
    {
        return sourceID;
    }

    public void setSourceID(String sourceID)
    {
        this.sourceID = sourceID;
    }

    public String getMarketSegmentOne()
    {
        return marketSegmentOne;
    }

    public void setMarketSegmentOne(String marketSegmentOne)
    {
        this.marketSegmentOne = marketSegmentOne;
    }

    /**
     * @return the bypassClientService
     */
    public String getBypassClientService()
    {
        return bypassClientService;
    }

    /**
     * @param bypassClientService the bypassClientService to set
     */
    public void setBypassClientService(String bypassClientService)
    {
        this.bypassClientService = bypassClientService;
    }

    public ClientInfoVO getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientInfoVO clientInfo) {
		this.clientInfo = clientInfo;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getLandingPage() {
		return landingPage;
	}

	public void setLandingPage(String landingPage) {
		this.landingPage = landingPage;
	}

}
