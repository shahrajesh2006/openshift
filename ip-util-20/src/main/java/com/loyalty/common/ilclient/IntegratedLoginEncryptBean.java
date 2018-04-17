package com.loyalty.common.ilclient;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.Provider;
import java.security.Security;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import com.loyalty.web.application.ApplicationContextProvider;
import com.loyalty.web.constants.Constants;

/**
 * This program encrypts the token and sends the encrypted token to integrated
 * login decryption page.
 */

public class IntegratedLoginEncryptBean {
	private String userName = "";
	private String webAppName = "";
	private String vector = "";
	private String authURL = "";
	private String pingURL = "";
	private String logoutURL = "";
	private String jSecurityCheckURL = "";
	private String partnerCode = "";
	final static Logger log = LoggerFactory.getLogger(IntegratedLoginEncryptBean.class);
	private String emailcmp = "";
	private String redirectTo = "";
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	private DBUtil DBUtil = null;
	
	public void process() throws IOException {
		if (request.getMethod().equals(RequestMethod.GET.toString())) {
			log.info("Get request ignored");
			return;
		}
		DBUtil = ApplicationContextProvider.getContext().getBean(DBUtil.class);
		java.util.Date cur_time = new java.util.Date();
		// Format the current time.
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String dateString = formatter.format(cur_time);

		// create token
		String token = "ss=" + getUserName() + "&ts=" + dateString;
		String partnerCode = (String) request.getParameter("partnerCode");
		byte[] encryptedToken;
		// Encrypt token
		try {
			encryptedToken = this.encrypt(getWebAppName(), token, partnerCode);
		} catch (Exception e) {
			log.error("IntegratedLoginEncryptBean: process(): ", e);
			return;
		}
		String hexToken = this.toHexString(encryptedToken);
		request.setAttribute(IntegratedLoginConstants.INIT_VECTOR_PARAMETER_NAME, getVector());
		request.setAttribute(IntegratedLoginConstants.AUTH_URL_PARAMETER_NAME, authURL);
		request.setAttribute(IntegratedLoginConstants.PING_URL_PARAMETER_NAME, pingURL);
		request.setAttribute(IntegratedLoginConstants.LOGOUT_URL_PARAMETER_NAME, getLogoutURL());
		request.setAttribute(IntegratedLoginConstants.J_SECURITY_CHECK_PARAMETER_NAME, jSecurityCheckURL);
		// we have to use these "temp" request parms because weblogic snatches
		// j_username, and j_password from the request
		// no matter where the post is directed
		request.setAttribute(IntegratedLoginConstants.TEMP_J_USERNAME_PARAMETER_NAME, hexToken);
		request.setAttribute(IntegratedLoginConstants.TEMP_J_PASSWORD_PARAMETER_NAME, getWebAppName());
		request.setAttribute(IntegratedLoginConstants.EMAIL_CAMPAIGN, getEmailcmp());
		request.setAttribute(IntegratedLoginConstants.REDIRECT_TO, getRedirectTo());

		String forwardResource = "Transfer.jsp";

		if (request.getParameter("generic") == null) {
			forwardResource = "TransferTo_" + getWebAppName() + ".jsp";
			log.info("Since parameter \"generic\" is not in the request, the forward page is not Transfer.jsp, but "
					+ forwardResource);
		}
		// redirect to intermediate page
		this.forward(forwardResource);
	}

	/*
	 * Converts char to int
	 */

	private int char2int(char c) {
		if (c == '0') {
			return 0;
		} else if (c == '1') {
			return 1;
		} else if (c == '2') {
			return 2;
		} else if (c == '3') {
			return 3;
		} else if (c == '4') {
			return 4;
		} else if (c == '5') {
			return 5;
		} else if (c == '6') {
			return 6;
		} else if (c == '7') {
			return 7;
		} else if (c == '8') {
			return 8;
		} else if (c == '9') {
			return 9;
		} else if (c == 'A') {
			return 10;
		} else if (c == 'B') {
			return 11;
		} else if (c == 'C') {
			return 12;
		} else if (c == 'D') {
			return 13;
		} else if (c == 'E') {
			return 14;
		} else if (c == 'F') {
			return 15;
		} else {
			return -1;
		}
	}

	/*
	 * Converts hex digit to a byte
	 */

	private byte hex2byte(char high, char low) {
		int hi = 0;
		int lo = 0;
		int sum = 0;
		hi = char2int(high);
		lo = char2int(low);
		sum = hi * 16 + lo;
		if (sum > 127) {
			sum = sum - 256;
			return (byte) sum;
		} else {
			return (byte) sum;
		}
	}

	/*
	 * Converts hex string to a byte array
	 */

	private byte[] toByteArray(String hex) {
		int len = hex.length();
		int index = 0;
		int i = 0;
		byte[] byteArray = new byte[len / 2];
		for (i = 0; i < len - 1; i++) {
			byteArray[index] = hex2byte(hex.charAt(i), hex.charAt(i + 1));
			i = i + 1;
			index++;
		}
		return byteArray;
	}

	Map getKeyStoreByPartnerCode(String partnerCode) {
		Map keyStoreMap = new HashMap();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer();

		String key1 = "";
		String key2 = "";
		String key3 = "";
		String iv = "";

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Connection conn = DBUtil.getConnection(Constants.LOLA_DS);
		String METHOD_NAME = "KeystoreDAO: getPartnerKeyInitVector() ";
		try {
			// String to call DB
			sql.append(
					"SELECT UTL_RAW.CAST_TO_VARCHAR2(KS.KEY1) KEY1, UTL_RAW.CAST_TO_VARCHAR2(KS.KEY2) KEY2, UTL_RAW.CAST_TO_VARCHAR2(KS.KEY3) KEY3, ");
			sql.append(" UTL_RAW.CAST_TO_VARCHAR2(KS.INIT_VECTOR) INIT_VECTOR, PAR.CODE PAR_CODE ");
			sql.append(" FROM KEYSTORES KS, PARTNERS PAR ");
			sql.append(" WHERE PAR.CODE = ? AND KS.PARTNER = PAR.ID ");
			sql.append(" AND ((? BETWEEN KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE) ");
			sql.append(" OR (? >= KS.EFFECTIVE_DATE AND KS.EXPIRATION_DATE IS NULL)) ");

			// prepare stored procedure
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, partnerCode);
			stmt.setTimestamp(2, currentTime);
			stmt.setTimestamp(3, currentTime);
			rs = stmt.executeQuery();

			if (rs.next()) {
				key1 = rs.getString("KEY1");
				key2 = rs.getString("KEY2");
				key3 = rs.getString("KEY3");
				iv = rs.getString("INIT_VECTOR");

				Vector keyStoreVec = new Vector(4);
				keyStoreVec.addElement(key1);
				keyStoreVec.addElement(key2);
				keyStoreVec.addElement(key3);
				keyStoreVec.addElement(iv);
				keyStoreMap.put(rs.getString("PAR_CODE"), keyStoreVec);
			}

		} catch (SQLException ex) {
			log.error(METHOD_NAME + "<<EMERGENCY>> SQL Error: ", ex);
			throw new RuntimeException(METHOD_NAME + "Database Error");
		} finally {
			try {
				DBUtil.close(conn, rs, stmt);
			} catch (Exception e) {
			}
		}
		return keyStoreMap;
	}

	public Vector getKeyStore(String webApplicationName) {
		String METHOD_NAME = "KeyGeneration.getKeyStore() :";
		Connection conn = null;
		Statement stmt = null;
		String sql = "";
		Vector keyStoreVec = new Vector(4);
		String key1 = "";
		String key2 = "";
		String key3 = "";
		String initVector = "";
		log.debug(METHOD_NAME + "get keystore begin. ");
		try {
			// get db connection
			conn = DBUtil.getConnection(IntegratedLoginConstants.LOLA_DS);
			java.util.Date currentTime = new java.util.Date();
			// Format the current time.
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd h:m:sa");
			String currentTimeString = formatter.format(currentTime);
			// System.out.println("currentTimeString = " + currentTimeString);
			// String to call Stored Procedure
			sql = "SELECT UTL_RAW.CAST_TO_VARCHAR2(KEY1) KEY1,UTL_RAW.CAST_TO_VARCHAR2(KEY2) KEY2,UTL_RAW.CAST_TO_VARCHAR2(KEY3) KEY3,UTL_RAW.CAST_TO_VARCHAR2(INIT_VECTOR) INIT_VECTOR FROM KEYSTORES WHERE CLIENT_PROGRAM ="
					+ "(SELECT CLIENT_PROGRAM FROM APPLICATIONS WHERE NAME = '" + webApplicationName
					+ "') AND ((TO_DATE('" + currentTimeString
					+ "','YYYYMMDD HH:MI:SSAM') BETWEEN EFFECTIVE_DATE AND EXPIRATION_DATE) " + " OR (TO_DATE('"
					+ currentTimeString + "','YYYYMMDD HH:MI:SSAM') >= EFFECTIVE_DATE AND EXPIRATION_DATE IS NULL)) "
					+ " AND PARTNER IS NULL";
			// prepare stored procedure
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			log.info("Keystore query: " + sql);
			while (rs.next()) {
				key1 = rs.getString("KEY1");
				// System.out.println("KEY1: "+key1);
				key2 = rs.getString("KEY2");
				// System.out.println("KEY2: "+key2);
				key3 = rs.getString("KEY3");
				// System.out.println("KEY3: "+key3);
				initVector = rs.getString("INIT_VECTOR");
				// System.out.println("INIT_VECTOR: "+initVector);
			}
			if (this.getVector() != null && !this.getVector().equals("")) {
				initVector = this.getVector();
			}
			keyStoreVec.addElement(key1);
			keyStoreVec.addElement(key2);
			keyStoreVec.addElement(key3);
			keyStoreVec.addElement(initVector);
		} catch (SQLException ex) {
			log.error(METHOD_NAME + "<<EMERGENCY>> SQL Error: ", ex);
			throw new RuntimeException(METHOD_NAME + "Database Error");
		} finally {
			try {
				// clean up database
				stmt.close();
				conn.close();
			} catch (SQLException ex) {
				log.error(METHOD_NAME + "SQL Error: ", ex);
			}
		}
		log.debug(METHOD_NAME + "get keystore end. ");
		return keyStoreVec;
	}

	public byte[] encrypt(String webAppName, String text, String partnerCode) throws Exception {
		// System.out.println("---Begin TripleDES Encryption---");
		// Install SunJCE provider
		Provider sunJce = new com.sun.crypto.provider.SunJCE();
		Security.addProvider(sunJce);
		Vector keyStoreVec = new Vector();
		if (StringUtils.isNotBlank(partnerCode)) {
			Map keyStoreMap = getKeyStoreByPartnerCode(partnerCode);
			if (!keyStoreMap.isEmpty()) {
				keyStoreVec = (Vector) keyStoreMap.get(partnerCode);
			}
		} else {
			keyStoreVec = getKeyStore(webAppName);
		}
		String key1 = (String) keyStoreVec.elementAt(0);
		String key2 = (String) keyStoreVec.elementAt(1);
		String key3 = (String) keyStoreVec.elementAt(2);
		String initVector = (String) keyStoreVec.elementAt(3);
		byte[] ivBuffer = toByteArray(initVector);
		IvParameterSpec ivSpec = new IvParameterSpec(ivBuffer);
		byte[] token = text.getBytes();
		String wholeKey = key1 + key2 + key3;
		byte[] key = toByteArray(wholeKey);
		SecretKeySpec skeySpec = new SecretKeySpec(key, "DESede");
		AlgorithmParameters params = AlgorithmParameters.getInstance("DESede");
		params.init(ivSpec);
		Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, params);
		byte[] encrypted = cipher.doFinal(token);
		return encrypted;
	}

	/*
	 * Converts a byte to hex digit and writes to the supplied buffer
	 */

	private void byte2hex(byte b, StringBuffer buf) {
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}

	/*
	 * Converts a byte array to hex string
	 */

	private String toHexString(byte[] block) {
		StringBuffer buf = new StringBuffer();
		int len = block.length;
		for (int i = 0; i < len; i++) {
			byte2hex(block[i], buf);
		}
		return buf.toString();
	}

	protected void redirect(String redirectURL) throws IOException {
		this.response.sendRedirect(redirectURL);
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Wrapper method for the <code>RequestDispatcher</code> forward() method.
	 */
	protected void forward(String forwardResource) throws IOException {
		if (request == null) {
			return;
		} // if
		else if (response.isCommitted()) {
			log.debug(" The response was already commited, your forward to " + forwardResource + " is ignored.");
		} else {
			try {
				// reset the response to remove any previously set headers
				response.reset();
				RequestDispatcher rd = request.getRequestDispatcher(forwardResource);
				if (rd != null) {
					rd.forward(request, response);
				} else {
					// URL not found
					String errMsg = "Could not find requested resource. ";
					log.error(errMsg + forwardResource);
					throw new RuntimeException(errMsg);
				}
			} catch (javax.servlet.ServletException se) {
				String errMsg = "Error occured while processing your request.";
				log.error("ServletException occurred: " + se);
				throw new RuntimeException(errMsg);
			}
		}
	}

	public String getAuthURL() {
		return authURL;
	}

	public void setAuthURL(String authURL) {
		this.authURL = authURL;
	}

	public String getPingURL() {
		return pingURL;
	}

	public void setPingURL(String pingURL) {
		this.pingURL = pingURL;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getVector() {
		return vector;
	}

	public void setVector(String vector) {
		this.vector = vector;
	}

	public String getWebAppName() {
		return webAppName;
	}

	public void setWebAppName(String webAppName) {
		this.webAppName = webAppName;
	}

	public String getLogoutURL() {
		return logoutURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getRedirectTo() {
		return redirectTo;
	}

	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}

	public String getEmailcmp() {
		return emailcmp;
	}

	public void setEmailcmp(String emailcmp) {
		this.emailcmp = emailcmp;
	}

}
