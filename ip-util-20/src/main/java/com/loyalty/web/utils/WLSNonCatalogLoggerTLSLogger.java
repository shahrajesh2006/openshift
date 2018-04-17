package com.loyalty.web.utils;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loyalty.web.constants.TLSLogConstants;
import com.loyalty.web.errors.AbstractTLSLogger;
import com.loyalty.web.errors.TLSLoggerFactory;
import com.loyalty.web.errors.TLSLoggerInitializationException;
import com.loyalty.web.errors.TLSLoggerInterface;

/**
 * TLSLogger Class which wraps the Weblogic NonCatalogLogger
 * This is a useful class if you don't want to setup the a more complicated logger.
 *
 * @see AbstractTLSLogger
 * @see TLSLoggerFactory
 * @see TLSLoggerInterface
 * @author Douglas Morrison
 */
public class WLSNonCatalogLoggerTLSLogger extends AbstractTLSLogger
{
    // Private Member
    //private NonCatalogLogger logger;
    private final static Logger logger = LoggerFactory.getLogger(WLSNonCatalogLoggerTLSLogger.class);

    
    /**
     * Constructor used to initialize the logger, required by
     * TLSLoggerFactory class to allow initialization of the
     * TLSLogger class. Props must contain the Category.
     * 
     * @param props - used for initialization
     * @exception TLSLoggerInitializationException - signals initialization error
     */
    public WLSNonCatalogLoggerTLSLogger(HashMap props) throws TLSLoggerInitializationException
    {
        // Category must previous been initialized
        String cat = (String)props.get(TLSLogConstants.DefaultLogCategoryKey);
//        if(cat != null)
//            logger = new NonCatalogLogger(cat);
//        else
//            throw new TLSLoggerInitializationException("Could not determine log category.");
    }
    
    /**
     * Log fatal message.
     *
     * Since the NonCatalogLogger does not support fatal, these messages are 
     * redirected to the error level log
     *
     * @param message - message object to log
     */ 
    public void fatal(Object message)
    {
        if(logger != null)
        {
            logger.error(message.toString());
        }
    }    
    
    /**
     * Log fatal message.
     *
     * Since the NonCatalogLogger does not support fatal, these messages are 
     * redirected to the error level log
     *
     * @param message - message object to log
     */ 
    public void fatal(Object message, Throwable t)
    {
        if(logger != null)
        {
           logger.error(message.toString(),t);
        }
    }
    
    /**
     * Log error message.
     *
     * @param message - message object to log
     */ 
    public void error(Object message)
    {
        if(logger != null)
        {
            logger.error(message.toString());
        }
    }
    
    /**
     * Log error message.
     *
     * @param message - message object to log
     * @param t - thowable object
     */ 
    public void error(Object message, Throwable t)
    {
        if(logger != null)
        {
            logger.error(message.toString(), t);
        }
    }
        
    /**
     * Log warning message.
     *
     * @param message - message object to log
     */ 
    public void warn(Object message)
    {
        if(logger != null)
        {
            logger.warn(message.toString());
        }
    }
    
    /**
     * Log warning message.
     *
     * @param message - message object to log
     */ 
    public void warn(Object message, Throwable t)
    {
        if(logger != null)
        {
            logger.warn(message.toString(), t);
        }
    }
    
    /**
     * Log info message.
     *
     * @param message - message object to log
     */ 
    public void info(Object message)
    {
        if(logger != null && isInfoEnabled())
        {
            logger.info(message.toString());
        }
    }

    /**
     * Log info message.
     *
     * @param message - message object to log
     * @param t - thowable object
     */ 
    public void info(Object message, Throwable t)
    {
        if(logger != null && isInfoEnabled())
        {
            logger.info(message.toString(), t);
        }
    }
    
    /**
     * Log debug message.
     *
     * @param message - message object to log
     */ 
    public void debug(Object message)
    {
        if(logger != null && isDebugEnabled())
        {
            logger.debug(message.toString());
        }
    }
    
    /**
     * Log debug message.
     *
     * @param message - message object to log
     */ 
    public void debug(Object message, Throwable t)
    {
        if(logger != null && isDebugEnabled())
        {
            logger.debug(message.toString(), t);
        }
    }
}