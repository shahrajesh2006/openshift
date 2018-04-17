package com.loyalty.web.constants;

/**
 * <p>Brief Description : This class is used to define the list of
 *                        Error constants used by the  Lola Application
 * <p>Author Name       : Praveen M
**/

public final class ErrorConstants
{

	//JNDI LOOKUP EXCEPTIONS 000-199
	public static final String CLIENTSERVICE_JNDI_PROPERTY_NOT_FOUND = "001";
	public static final String SERVICE_CREATION_FAILED = "002";
	public static final String REMOTE_EXCEPTION = "003";
	public static final String SERVICE_LOOKUP_FAILED = "004";
	public static final String REMOTE_SERVICE_CREATE_EXCEPTION = "005";
	public static final String ACCOUNTSERVICE_JNDI_PROPERTY_NOT_FOUND = "008";
	public static final String DB_LOOKUP_NAMING_EXCEPTION = "010";
	public static final String REIMBURSEMENTSERVICE_JNDI_PROPERTY_NOT_FOUND = "17";

	//Account service error constants 990-1019
	public static final String INVALID_RETURNCODE="998"; // iadmin standardization
	public static final String SQLEXCEPTION_SAVEACCOUNT = "999"; // iadmin standardization
	public static final String DUPLICATE_ACCOUNT_LOGIN = "1000"; // iadmin standardization
	public static final String VID_ACCSRCH_SQLEXCEPTION = "1001";
	public static final String SEARCH_FAILED_WITH_TOO_MANY_ROWS = "1002";
	public static final String ACCOUNT_SEARCH_SQLEXCEPTION = "1003";
	public static final String EDIT_ACCOUNT_SQLEXCEPTION = "1004";
	public static final String EDIT_ACCOUNT_ROLLBACK_SQLEXCEPTION = "1005";
	public static final String SQLEXCEPTION_FINDACCOUNTBYACCTNBR = "1006";
    public static final String SQLEXCEPTION_SAVE_ACCOUNT_INQUIRY = "1007";
	public static final String VID_ACCOUNT_DOES_NOT_EXIST = "1008";
	public static final String DUPLICATE_ACCOUNT_NUMBER = "1009";// iadmin standardization
	public static final String DUPLICATE_CARD_NUMBER = "1010"; // idamin standardization
	public static final String SQLEXCEPTION_LOADACCOUNT = "1011";
	public static final String SQLEXCEPTION_LOADACCOUNT_ATTRIBUTES = "1012";
	//comment error THE FOLLOWING TWO ERRORS SHOULD BE REFACTORED TO BE IN THE ACCOUNT SERVICE RANGE ******
    public static final String SQLEXCEPTION_SAVECOMMENT=	"10110"; //iadmin standardization
    public static final String SQLEXCEPTION_GETCOMMENT	=	"10111"; // iadmin standardization
    public static final String ACCOUNT_NOT_FOUND = "10112";

    //redemption service constants 1020-1029
	public static final String VID_FINDREDEMPTIONS_SQLEXCEPTION = "1021";
	public static final String REDEMPTION_INACTIVE = "1022";
	public static final String REDEMPTION_NOT_FOUND = "1023";
	public static final String MULTIPLE_REDEMPTIONS="1024";
	public static final String REDEMPTION_INVALID = "1025";
	public static final String REDEMPTION_RESUBMIT_INVALID = "1026";
	public static final String REDEMPTION_VOID_INVALID = "1027";
	
	//transactionservice error constants 1031-1039
	public static final String SQLEXCEPTION_LOADACCTSUMMARYTRANSACTIONS = "1031";
	public static final String SQLEXCEPTION_SAVE_TRANSACTION = "1032";
	public static final String SQLEXCEPTION_LOADACCTDETAILTRANSACTIONS = "1033";

	//site service error constants 1040-1049
	public static final String SQLEXCEPTION_LOADSITECODES = "1041";
	public static final String SQLEXCEPTION_LOADSITES = "1042";
	public static final String SQLEXCEPTION_EDITSITE = "1043";
	public static final String SQLEXCEPTION_REOPENSITE = "1044";
	public static final String SQLEXCEPTION_CLOSESITE = "1045";
	public static final String SQLEXCEPTION_ADDSITE = "1046";
	public static final String SQLEXCEPTION_VIEWSITE = "1047";
	public static final String SITE_INACTIVE = "1048";
	public static final String MORE_THAN_ONE_SITE_FOUND = "1048";
    public static final String SITE_NOT_FOUND = "1049";

	//Membership number service error constants 1050-1059
	public static final String MEMBERSHIPNUMBER_SEARCH_SQLEXCEPTION = "1051";
	public static final String MEMBERSHIPNUMBER_EXPIRE_SQLEXCEPTION = "1052";
	public static final String MEMBERSHIPNUMBER_VALIDATE_SQLEXCEPTION = "1053";
	public static final String MEMBERSHIPNUMBER_EXPIRE_ROLLBACK_SQLEXCEPTION = "1054";
	public static final String MEMBERSHIPNUMBER_ADD_SQLEXCEPTION = "1055";
	public static final String MEMBERSHIPNUMBER_ADD_ROLLBACK_SQLEXCEPTION = "1056";
	public static final String MEMBERSHIPNUMBER_VALIDATE_ABSENT = "1057";
	public static final String MEMBERSHIPNUMBER_VALIDATE_USED = "1058";
	public static final String SQLEXCEPTION_SAVECARDNUMBER = "1059";//iadmin standardization

	//error constants for client service 1060-1069
	public static final String SQLEXCEPTION_LOADPORTTRANSCODES = "1061";
	public static final String SQLEXCEPTION_LOADPPARNTERS = "1062";
	public static final String SQLEXCEPTION_GETDOINGBUSINESSAS="1063"; // iadmin standardization

	//Utility and Portfolio Service error constants 1070-1079
	public static final String SQLEXCEPTION_LOADHEARDABTRSNS = "1071";
	public static final String SQLEXCEPTION_LOADCURRENCYCODES = "1072";
	public static final String SQLEXCEPTION_FINDCOUNTRYCODE="1073"; // iadmin standardization
	public static final String SQLEXCEPTION_GETHEARDFROMREASON="1074"; // iadmin standardization
	public static final String SQLEXCEPTION_GETPORTFOLIOCODES="1075";// iadmin standardization
	public static final String SQLEXCEPTION_GETSTATUSTYPES="1076";//   iadmin standardization
	
	public static final String SQLEXCEPTION_NOCARTID = "1400";
	public static final String SQLEXCEPTION_NOLOG = "1500";
	public static final String SQLEXCEPTION_NOTWEBCHANNEL = "1600";
	public static final String SQLEXCEPTION_NOTVALIDCARTID = "1700";

	//Customer service error constants 1080-1089
	public static final String SQLEXCEPTION_EDIT_EMAIL = "1081";
	public static final String EDIT_MAIL_MORE_ROWS_UPDATED = "1082";
    public static final String SQLEXCEPTION_SAVECUSTOMER="1083";// iadmin standardization

	//account attribute service error constants 1090-1099
	public static final String SQLEXCEPTION_FETCH_ACCT_ATTRIBUTES = "1091";
	public static final String SQLEXCEPTION_EDIT_ACCT_ATTRIBUTES = "1092";
	public static final String SQLEXCEPTION_EDIT_ACCT_ATTRIBUTES_ROLLBACK = "1093";
	public static final String ACCT_ATTRIB_ALREADY_EXISTS = "1094";

	//general error constants 1100-1119
	public static final String CONCURRENT_MODIFICATION_ERROR = "1101";
	public static final String TOOMANYROWS_UPDATE = "1102";
	public static final String ACCOUNT_SERVICE_FAILURE = "1103";
	public static final String ACCOUNT_MBR_NBR_SERVICE_FAILURE = "1104";
	public static final String ACCOUNT_TRANS_SERVICE_FAILURE = "1105";
	public static final String CLIENT_SERVICE_TRANS_FAILURE = "1106";
	public static final String CUSTOMER_SERVICE_FAILURE = "1107";
	public static final String REDEMPTION_SERVICE_TRANS_FAILURE = "1108";
	public static final String UTILITY_SERVICE_TRANS_FAILURE = "1109";
	public static final String SITE_SERVICE_FAILURE = "1110";
	public static final String REDEMPTION_SERVICE_FAILURE="1111";
	public static final String VEHICLE_SERVICE_FAILURE = "1112";
	public static final String XMLERROR_SERVICE_FAILURE = "1113";

    //account merchant service error constants 1120-1129
    public static final String SQL_EXCEPTION_FETCH_ACCT_MERCHANTS = "1120";


	//partner service error constants 1130-1139
	public static final String PARTNER_SERVICE_SEARCH_FAILURE = "1130";

    //shopping cart service constants 1030-1030
	public static final String REDEMPTION_QTY_MAX_EXCEEDED = "1030";

    //Database Errors 10050-10099
    public static final String SQL_EXCEPTION_CONNECTION = "10050";
	public static final String CONNECTION_CLOSE_ERROR = "10051";
	public static final String SQLEXCEPTION_FINDINQUIRYCODES="1077";
	
    //General Errors 10500-10700
    public static final String UNDEFINED_ERROR = "10500";
	public static final String SYSTEM_ERROR = "10501";
	public static final String NAMING_EXCEPTION = "10502";
	public static final String SERVICE_CONNECTION_FAILED = "10503";
	public static final String CONNECTION_NAMING_EXCEPTION = "10504";
	public static final String UNDEFINED_DB_EXCEPTION = "10505";
	public static final String VID_UNDEFINED_PROMO_RULE = "10506";
	public static final String MISSING_REQUIRED_DATA = "10507";

    //HotelStayEarnings Errors 1130
    public static final int ERROR_CODE =1130;
    public static final String SQL_EXCEPTION ="DATABASE_ERROR";

    //DealerWeb Constatnts 1140
    public static final String PURCHASERLAST4SSN_NULL="1040";
    public static final String PURCHASERDOB_NULL="1041";
    public static final String PURCHASERLAST4SSN_MISMATCH="1042";
    public static final String PURCHASERDOB_MISMATCH="1043";

    //Reservation service constants 1150-1159
    public static final String RESERVATION_NOT_FOUND = "1150";
    public static final String RESERVATION_INACTIVE = "1151";
    public static final String MULTIPLE_RESERVATIONS = "1152";
    public static final String RESERVATION_SITE_MISMATCH = "1153";
    public static final String RESERVATION_SERVICE_CREATION_FAILURE = "1154";
    public static final String RESERVATION_SERVICE_CANCELLATION_FAILURE = "1155";    
    public static final String RESERVATION_ALREADY_EXISTS = "1156";    
    public static final String RESERVATION_RESUBMIT_TOO_SOON = "1157"; 
    
    //Fax Service error Constants 1200 - 1219
    public static final String BAD_USER_PHONE_NUMBER = "1200";
    public static final String BAD_DEFAULT_PHONE_NUMBER = "1201";
    public static final String FAXNUMBER_NOT_FOUND = "1202";

    //Client_Letter error constants (1220-1240)
    public static final String LETTERTYPE_NOT_FOUND = "1220";
    
    //More site service errors 1241-1260
    public static final String CREATED_SITE_ALREDY_EXISTS = "1242";
    
    // Constants for employee site
    public static final String USER_INVALID = "1250";
    public static final String USER_INELIGIBLE = "1251";
    
    //Constants for account suspensions 1270-1290
    public static final String ACCOUNT_SUSPENSION_SERVICE_FAILURE = "1270";
    public static final String ACCOUNT_SUSPENSION_ALREADY_EXISTS = "1271";
    public static final String ACCOUNT_SUSPENSION_EFF_DATE_TOO_EARLY = "1272";
    public static final String ACCOUNT_SUSPENSION_EXP_DATE_GRTR = "1273";
    public static final String ACCOUNT_SUSPENSION_INVOL_OVERR = "1274";
    public static final String ACCOUNT_SUSPENSION_INVOL_OVERL = "1275";
    
    
    public static final String FORGOTPASSOWRD_USERNAME_NULL = "forgotpassword.username.null";
    public static final String FORGOTPASSOWRD_USERNAME_NOT_FOUND = "forgotpassword.username.notfound";
    public static final String FORGOTPASSOWRD_SECRETANSWER_INVALID = "forgotpassword.secretanswer.invalid";
    public static final String FORGOTPASSWORD_SECRETANSWER_LASTTRY = "forgotpassword.secretanswer.lasttry";
    public static final String FORGOTPASSOWRD_SECRETANSWER_MAXATTEMPTS = "forgotpassword.secretanswer.maxattempts";
    public static final String FORGOTPASSOWRD_SECRETANSWER_LOCKOUT = "forgotpassword.secretanswer.lockout";
    public static final String FORGOTPASSOWRD_SECRETANSWER_NULL = "forgotpassword.secretanswer.null";
    public static final String FORGOTPASSOWRD_PASSWORD_NULL = "forgotpassword.password.null";
    public static final String FORGOTPASSOWRD_PASSWORD_LENGTH = "forgotpassword.password.length";
    public static final String FORGOTPASSOWRD_PASSWORD_VERIFY_NULL = "forgotpassword.password.verify.null";
    public static final String FORGOTPASSOWRD_PASSWORD_VERIFY_MISMATCH = "forgotpassword.password.verify.mismatch";

    //Business validation constants 1300 
	public static final String INVALID_ACCOUNT_ID = "1310";
	public static final String INVALID_PORTFOLIO_ID = "1301";
	public static final String INVALID_ACCOUNT = "1302";
	public static final String INVALID_REDEMPTION_GRACE = "1314";
	public static final String INVALID_REDEMPTION_ACCOUNT = "1315";
	public static final String INVALID_REDEMPTION_TYPE = "1316";
	public static final String INVALID_PARAMETER = "1300";
    
    //Earnings  Error Constants EXPIRE_DATE_EARLY
    public static final String INVALID_EXPIRE_DATE = "1350"; 
    public static final String EXPIRE_DATE_EARLY = "1350";
    public static final String TRANS_CODES_NOT_FOUND = "1351";
    public static final String SQLEXCEPTION_EDIT_ACCTTRANS_FAILURE ="1352";
    public static final String REDEMPTION_STATS_NOT_FOUND ="1353";
    public static final String INVALID_TRANSACTION_DATE = "1354";
    public static final String INVALID_WHOLE_NUMBER = "1355";
    
    public static final String FIND_EARNINGS_STMT_HISTORY_ERROR = "1360";
    public static final String FIND_EARNINGS_STMT_HISTORY_NOT_FOUND = "1361";
    
    public static final String FIND_EARNINGS_SUMMARY_ERROR ="1370";
    public static final String FIND_EARNINGS_SUMMARY_NOT_FOUND ="1371";
    
    public static final String FIND_AVAILABLE_EARNINGS_ERROR = "1380";
    public static final String FIND_AVAILABLE_EARNINGS_NOT_FOUND ="1381";
    
    public static final String FIND_EARNINGS_UPCOMING_EXPIRATIONS_ERROR = "1390";
    public static final String FIND_EARNINGS_UPCOMING_EXPIRATIONS_NOT_FOUND ="1391";
   
    public static final String FIND_EARNINGS_ANNIVERSARY_YEAR_ERROR = "1395";
    public static final String FIND_EARNINGS_ANNIVERSARY_YEAR_NOT_FOUND ="1396";
   
    // VTS Error Constants
    public static final String VTS_INELIGIBLE_ERROR = "VTS001";
    public static final String VTS_ACTIVE_TAG_NOT_FOUND ="VTS002";
    public static final String VTS_VIN_EXSIST ="VTS003";
    public static final String VTS_VEHICLE_ALREADY_TAGGED_ERROR = "VTS004";
    public static final String VTS_SEC_PASSWORD_MISMATCH = "redemption.secondarypassword.mismatch";
    
    public static final String WISH_LIST_ITEM_ALREADY_EXISTS = "WL001";
    
//    Advertisement Errors
    public static final String AD_SRCH_SQLEXCEPTION = "AD001";
    public static final String AD_RESPONSE_FAILURE = "AD002";
    
    // CrossPlatform Point Transfer Error
    public static final String SQLEXCEPTION_XPLATFORM_TRANS_POINTS_ERROR = "1397";
    
}
