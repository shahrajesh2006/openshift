package com.loyalty.web.errors;

/**
 * Abstract class which implement the TLSLoggerInterface, all 
 * TLSLogger implementations should extend this class
 * 
 * @see TLSLoggerInterface
 *
 * @author Douglas Morrison
 */
abstract public class AbstractTLSLogger implements TLSLoggerInterface
{
    private boolean debugEnabled=true;
    private boolean infoEnabled =true;
    abstract public void fatal(Object message);
    abstract public void fatal(Object message, Throwable t);
    abstract public void error(Object message);
    abstract public void error(Object message, Throwable t);
    abstract public void warn(Object message);
    abstract public void warn(Object message, Throwable t);
    abstract public void info(Object message);
    abstract public void info(Object message, Throwable t);
    abstract public void debug(Object message);
    abstract public void debug(Object message, Throwable t);
    
    
    public void setDebugEnabled(boolean debugEnabled)
    {
     this.debugEnabled=debugEnabled;	
    }
    
    public void setInfoEnabled(boolean infoEnabled)
    {
     this.infoEnabled=infoEnabled;		
    }
    
    public boolean isDebugEnabled()
    {
     return this.debugEnabled;	
    }
    
    public boolean isInfoEnabled()
    {
     return this.infoEnabled;	
    }
    
    
}