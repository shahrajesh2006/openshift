package com.loyalty.web.constants;

/** TLSLog Constants for configuration information for the TLSLogFactory and TLSLoggers */
public class TLSLogConstants
{
    /** private constructors prohibits creation of this class */
    private TLSLogConstants() { };

    // Values used in hashmap passed to logger constructor
    final public static String DefaultLogCategoryKey = "Category";
    private static String DefaultLogCategory = "IPRO";
    //Default Logger class
    private static String DefaultTLSLoggerClass = "com.loyalty.web.utils.WLSNonCatalogLoggerTLSLogger";

    static void setDefaultLogCategory(String DefaultLogCategory)
    {
       TLSLogConstants.DefaultLogCategory = DefaultLogCategory;
    }

    public static String getDefaultLogCategory()
    {
        return DefaultLogCategory;
    }

    static void setDefaultTLSLoggerClass(String DefaultTLSLoggerClass)
    {
       TLSLogConstants.DefaultTLSLoggerClass = DefaultTLSLoggerClass;
    }

    public static String getDefaultTLSLoggerClass()
    {
        return DefaultTLSLoggerClass;
    }
}
