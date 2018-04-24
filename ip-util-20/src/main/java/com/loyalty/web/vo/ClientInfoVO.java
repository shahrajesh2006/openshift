package com.loyalty.web.vo;

import com.loyalty.web.utils.ReflectionUtils;


/**
 * @author praveenmanjunatha
 * @since Aug 2, 2004
 *
 * This class stores client related information
 */
public class ClientInfoVO implements TLSVO
{ 
	static final long serialVersionUID = -8457582032070447456L;


	private Long clientID;
	private String clientName;

	private Long clientProgramID;
	private String clientProgramName;

	private Long portfolioID;
	private String portfolioName;
	
	private String clientProgramCode;
    
    private String portfolioCode;
    
    public ClientInfoVO() {

	}

	public ClientInfoVO(Long clientProgramID) {
	    this.clientProgramID = clientProgramID;
	}

	/**
	 * Returns the clientID.
	 * @return Long
	 */
	public Long getClientID()
	{
		return clientID;
	}

	/**
	 * Returns the clientName.
	 * @return String
	 */
	public String getClientName()
	{
		return clientName;
	}

	/**
	 * Returns the clientProgramID.
	 * @return Long
	 */
	public Long getClientProgramID()
	{
		return clientProgramID;
	}

	/**
	 * Returns the clientProgramName.
	 * @return String
	 */
	public String getClientProgramName()
	{
		return clientProgramName;
	}

	/**
	 * Returns the portfolioID.
	 * @return Long
	 */
	public Long getPortfolioID()
	{
		return portfolioID;
	}

	/**
	 * Returns the portfolioName.
	 * @return String
	 */
	public String getPortfolioName()
	{
		return portfolioName;
	}

	/**
	 * Sets the clientID.
	 * @param clientID The clientID to set
	 */
	public void setClientID(Long clientID)
	{
		this.clientID = clientID;
	}

	/**
	 * Sets the clientName.
	 * @param clientName The clientName to set
	 */
	public void setClientName(String clientName)
	{
		this.clientName = clientName;
	}

	/**
	 * Sets the clientProgramID.
	 * @param clientProgramID The clientProgramID to set
	 */
	public void setClientProgramID(Long clientProgramID)
	{
		this.clientProgramID = clientProgramID;
	}

	/**
	 * Sets the clientProgramName.
	 * @param clientProgramName The clientProgramName to set
	 */
	public void setClientProgramName(String clientProgramName)
	{
		this.clientProgramName = clientProgramName;
	}

	/**
	 * Sets the portfolioID.
	 * @param portfolioID The portfolioID to set
	 */
	public void setPortfolioID(Long portfolioID)
	{
		this.portfolioID = portfolioID;
	}

	/**
	 * Sets the portfolioName.
	 * @param portfolioName The portfolioName to set
	 */
	public void setPortfolioName(String portfolioName)
	{
		this.portfolioName = portfolioName;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return ReflectionUtils.toString(this);
	}
    /**
     * @return Returns the clientProgramCode.
     */
    public String getClientProgramCode() {
        return clientProgramCode;
    }
    /**
     * @param clientProgramCode The clientProgramCode to set.
     */
    public void setClientProgramCode(String clientProgramCode) {
        this.clientProgramCode = clientProgramCode;
    }

    public String getPortfolioCode()
    {
        return portfolioCode;
    }

    public void setPortfolioCode(String portfolioCode)
    {
        this.portfolioCode = portfolioCode;
    }

	public String getDisplayDiscription() {
		return  clientProgramID + " - " + clientName + " - " + clientProgramName;
	}
	
}
