package com.loyalty.web.errors;
/**
 * TLS Logger Implementations must implement this interface. To
 * insure this, the logger implementations should extend the 
 * AbstractTLSLogger.
 *
 * @see AbstractTLSLogger
 * @see TLSLoggerFactory
 *
 * @author Douglas Morrison
 */
public interface TLSLoggerInterface
{
    public void fatal(Object message);
    public void fatal(Object message, Throwable t);
    public void error(Object message);
    public void error(Object message, Throwable t);
    public void warn(Object message);
    public void warn(Object message, Throwable t);
    public void info(Object message);
    public void info(Object message, Throwable t);
    public void debug(Object message);
    public void debug(Object message, Throwable t);
    public void setDebugEnabled(boolean debugEnabled);
    public void setInfoEnabled(boolean debugEnabled);
    public boolean isDebugEnabled();
    public boolean isInfoEnabled();
    
}