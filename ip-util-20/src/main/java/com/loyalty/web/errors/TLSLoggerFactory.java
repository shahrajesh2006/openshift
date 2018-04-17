package com.loyalty.web.errors;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.loyalty.web.constants.TLSLogConstants;

/**
 * Factory class to create the TLSLogger Implementation Classes
 *
 * @see TLSLoggerInterface
 * @see AbstractTLSLogger
 *
 * @author Douglas Morrison
 **/
public final class TLSLoggerFactory
{
    /**
     * private constructor to prohibit instantiation
     */
    private TLSLoggerFactory()
    {
    };

    private static Class loggerClass = null;

    private static boolean log4JTLSLoggerAvailable; //This property is set to true when application is using log4j logging

// Rajesh - Commenting out static Initializer due to issue of using log4j in multiple cluster deployment scenario
//    static
//    // static initializer attempts to initialize log4j
//    {
//        if (!log4JTLSLoggerAvailable)
//        {//If not already initialized
//            attemptToInitializeLog4J(); // Give one try to initialize log4j
//        }
//    }

    /**
     * Get the TLSLogger that is default for the application
     * with the default category
     *
     * Usage: TLSLoggerInterface tlslogger = TLSLoggerFactory.getTLSLogger();
     *
     * @return TLSLoggerInterface - interface to logger class can be null
     */
    public static TLSLoggerInterface getTLSLogger()
    {
        return getTLSLogger(TLSLogConstants.getDefaultLogCategory());
    }

    /**
     * Get the TLSLogger that is default for the application
     * with the specified category
     *
     * Usage: TLSLoggerInterface tlslogger = TLSLoggerFactory.getTLSLogger("CategoryName");
     *
     * @param String category - category to log to
     * @return TLSLoggerInterface - interface to logger class can be null
     */
    public static TLSLoggerInterface getTLSLogger(String category)
    {
        // check to see if the category is null
        if (category == null)
            throw new IllegalArgumentException("Category must not be null");

        // Create the props hashmap which is filled with default props 
        // for the logger.
        HashMap props = new HashMap();
        props.put(TLSLogConstants.DefaultLogCategoryKey, category);

        // Rather than declaring the class here create 
        // a new class by name from the Log Config Constants
        // All class that are a TLS logger must implement the 
        // constructor which takes a HashMap
        TLSLoggerInterface logger = null;

        // Check if application initialized log4j.  return log4tlslogger instead of weblogic non catalog logger
        if (isLog4JTLSLoggerAvailable())
        {

            logger = getLog4JTLSLogger(props);
        }

        if (logger != null)
        {
            return logger;
        }

        try
        {
            // Create an instance of the TLSLogger class which is specified
            // by the DefaultTLSLoggerClass constant

            // setup the construct params 
            Class[] loggerConsParamType = new Class[] { HashMap.class };
            Object[] args = new Object[] { props };

            // create the class object and get its constructor
            if (loggerClass == null)
            {
                loggerClass = Class.forName(TLSLogConstants.getDefaultTLSLoggerClass());
            }
            Constructor loggerConstructor = loggerClass.getConstructor(loggerConsParamType);

            // create an instnace of the class and set the logger to the new instance
            logger = (TLSLoggerInterface) loggerConstructor.newInstance(args);
        }
        // ERROR CONDITIONS RETURN NULL
        catch (ClassNotFoundException e)
        {
            System.err.println("TLSLoggerFactory: " + TLSLogConstants.getDefaultTLSLoggerClass() + " Class Not Found!");
        }
        catch (NoSuchMethodException e)
        {
            System.err.println("TLSLoggerFactory: " + TLSLogConstants.getDefaultTLSLoggerClass() + " Constructor Not Found!");
        }
        catch (InstantiationException e)
        {
            System.err.println("TLSLoggerFactory: " + TLSLogConstants.getDefaultTLSLoggerClass() + " Instantiation Error!");
        }
        catch (IllegalAccessException e)
        {
            System.err.println("TLSLoggerFactory: " + TLSLogConstants.getDefaultTLSLoggerClass() + " Illegal Access!");
        }
        catch (IllegalArgumentException e)
        {
            System.err.println("TLSLoggerFactory: " + TLSLogConstants.getDefaultTLSLoggerClass() + " Illegal Argument!");
        }
        catch (InvocationTargetException e)
        {
            System.err.println("TLSLoggerFactory: " + TLSLogConstants.getDefaultTLSLoggerClass() + " Invocation Error!");
        }
        // return the logger interface - it can be null
        return logger;
    }


    private static TLSLoggerInterface getLog4JTLSLogger(HashMap props)
    {

        TLSLoggerInterface log4JTLSLogger = null;
        try
        {
            log4JTLSLogger = new Log4JTLSLogger(props);
        }
        catch (Exception e)
        {
            System.err.println("TLSLoggerFactory: Log4JTLSLogger " + e.getMessage());
        }


        return log4JTLSLogger;
    }

    public static boolean isLog4JTLSLoggerAvailable()
    {
        return log4JTLSLoggerAvailable;
    }

    public static void setLog4JTLSLoggerAvailable(boolean log4JTLSLoggerAvailable)
    {
        TLSLoggerFactory.log4JTLSLoggerAvailable = log4JTLSLoggerAvailable;
    }

}