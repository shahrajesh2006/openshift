package com.loyalty.web.vo;

public class OMASSOPayloadVO implements TLSVO
{
    private static final long serialVersionUID = 1L;

    /** The account number of the account being serviced */
    private String accountNumber;

    /** Some client identifer that doesn't mean anything to us, but is sent back to them when creating a redemption */
    private String vendorID;

    /** Some client identifer that doesn't mean anything to us, but is sent back to them when creating a redemption */
    private String sourceID;

    /** The segment of the account being serviced.  Never actually used, since the xml callback retrieves and processes this */
    private String marketSegmentOne;
    
    /** The session id on the partner's server */
    private String sessionID;

    /** The offer code for deep linking */
    private String offerCode;

    /** Auth URL to use when login fails */
    private String authURL;
    
    /** Ping URL to keep session alive on partner's server */
    private String pingURL;
    
    /** Logout url to terminate session on partner's server */
    private String logoutURL;

    /** Site identifier for site affinity on partner's server */
    private String siteID;
    
    /** True means we don't call the client's service upon initial sign-in, so that we can test access to app without depending on client */
    private String bypassClientService;
    
    /** Category Code for merchandise/exclusives */
    private String catCode;
    
    /** External Source  - Added for DFS Twitter */
    private String externalSource;
    
    
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

    public String getSessionID()
    {
        return sessionID;
    }

    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }

    public String getOfferCode()
    {
        return offerCode;
    }

    public void setOfferCode(String offerCode)
    {
        this.offerCode = offerCode;
    }

    public String getAuthURL()
    {
        return authURL;
    }

    public void setAuthURL(String authURL)
    {
        this.authURL = authURL;
    }

    public String getPingURL()
    {
        return pingURL;
    }

    public void setPingURL(String pingURL)
    {
        this.pingURL = pingURL;
    }

    public String getLogoutURL()
    {
        return logoutURL;
    }

    public void setLogoutURL(String logoutURL)
    {
        this.logoutURL = logoutURL;
    }

    public String getSiteID()
    {
        return siteID;
    }

    public void setSiteID(String siteID)
    {
        this.siteID = siteID;
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

    /**
     * @return the catCode
     */
    public String getCatCode()
    {
        return catCode;
}

    /**
     * @param catCode the category code
     */
    public void setCatCode(String catCode)
    {
        this.catCode = catCode;
    }

    public String getExternalSource()
    {
        return externalSource;
}

    public void setExternalSource(String externalSource)
    {
        this.externalSource = externalSource;
    }
}
