package com.loyalty.web.errors;

/**
 * Execption Class used to by TLSLogger Implementations to 
 * signal that an error ocurred during initialization
 *
 * @ see TLSLoggerFactory
 * @ see AbstractTLSLogger
 *
 * @ author Douglas Morrison
 * 
 */
public class TLSLoggerInitializationException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6680910339223215057L;

	TLSLoggerInitializationException()
    {
        super();
    }
    
    TLSLoggerInitializationException(String msg)
    {
        super(msg);
    }
}