package com.loyalty.web.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthUtils
{
	final static Logger logger = LoggerFactory.getLogger(AuthUtils.class);
    
    public static String generateTimestamp()
    {
        Date sysdate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String internalTimestamp = formatter.format(sysdate);

        return internalTimestamp;
    }
    
    public static boolean isTimestampValid(String timestamp)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date date = formatter.parse(timestamp);
            Date sysdate = new Date();
            
            return sysdate.getTime() - date.getTime() <= 1000 * 60 * 5;
        }
        catch (Exception x)
        {
        	logger.error("Error while attempting to validate timestamp.", x);
        }
        
        return false;
    }

    public static String getValue(String value, String defaultValue)
    {
        return StringUtils.isBlank(value) ? defaultValue : value;
    }
}
