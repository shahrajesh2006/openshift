package com.loyalty.web.errors;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loyalty.web.constants.Constants;
import com.loyalty.web.constants.TLSLogConstants;
/**
 * Implementation class which provides logging facilities
 * via the Log4J Framework.
 *
 * @see AbstractTLSLogger
 * @see TLSLoggerFactory
 * @see TLSLoggerInterface
 * @see Log4JTLSLoggerStartupServlet
 *
 * @author Douglas Morrison
 */
final public class Log4JTLSLogger extends AbstractTLSLogger
{
    // Log4J Logger Member
    private Logger log4JLogger;

    /**
     * Constructor used to initialize the logger, required by
     * TLSLoggerFactory class to allow initialization of the
     * TLSLogger class. Props must contain the Category.
     * 
     * @param props - used for initialization
     * @exception TLSLoggerInitializationException - signals initialization errors
     */
    public Log4JTLSLogger(HashMap props) throws TLSLoggerInitializationException
    {
        // Category must previously been initialized
        // This is done via the Log4JTLSLoggerStartupServlet
        String cat = (String)props.get(TLSLogConstants.DefaultLogCategoryKey);
        if(cat != null)
            log4JLogger = LoggerFactory.getLogger(cat);
        else
            throw new TLSLoggerInitializationException("Could not determine log category.");
    }
        
    /**
     * Log fatal message.
     *
     * @param message - message object to log
     */ 
    public void fatal(Object message)
    {
        if(log4JLogger != null)
        {
            log4JLogger.error((String) message);
        }
    }    
    
    /**
     * Log fatal message.
     *
     * @param message - message object to log
     */ 
    public void fatal(Object message, Throwable t)
    {
        if(log4JLogger != null)
        {
            log4JLogger.error((String) message,t);
        }
    }
    
    /**
     * Log error message.
     *
     * @param message - message object to log
     */ 
    public void error(Object message)
    {
        if(log4JLogger != null)
        {
            log4JLogger.error((String) message);
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
        if(log4JLogger != null)
        {
            log4JLogger.error((String) message, t);
        }
    }
        
    /**
     * Log warning message.
     *
     * @param message - message object to log
     */ 
    public void warn(Object message)
    {
        if(log4JLogger != null)
        {
            log4JLogger.warn((String) message);
        }
    }
    
     /**
     * Log warning message.
     *
     * @param message - message object to log
     */ 
    public void warn(Object message, Throwable t)
    {
        if(log4JLogger != null)
        {
            log4JLogger.warn((String) message, t);
        }
    }
    
    /**
     * Log info message.
     *
     * @param message - message object to log
     */ 
    public void info(Object message)
    {
        if(log4JLogger != null)
        {
            log4JLogger.info((String) message);
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
        if(log4JLogger != null)
        {
            log4JLogger.info((String) message, t);
        }
    }
    
    /**
     * Log debug message.
     *
     * @param message - message object to log
     */ 
    public void debug(Object message)
    {
        if(log4JLogger != null)
        {
            log4JLogger.debug((String) message);
        }
    }
    
    /**
     * Log debug message.
     *
     * @param message - message object to log
     */ 
    public void debug(Object message, Throwable t)
    {
        if(log4JLogger != null)
        {
            log4JLogger.debug((String) message, t);
        }
    }
    
    // Utility method to get TLSLogger object
    public static Log4JTLSLogger getInstance(String category){
       
       Log4JTLSLogger log=null;
       
       if(category == null || "".equals(category))
       {
           category = Constants.COMMON_LOG_NAME;
       }
       HashMap props = new HashMap();
       props.put(TLSLogConstants.DefaultLogCategoryKey, category);

       try
       {
           //Get the Log4J Logger.
           log = new Log4JTLSLogger(props);
       }
       catch(Exception e)
       {
          throw new RuntimeException(e.getMessage());
       }
       
       return log;
    }
}