package com.loyalty.web.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.loyalty.common.ilclient.DBUtil;
import com.loyalty.web.constants.Constants;

@Controller
public class ILDirectorActionController {
	
	@Autowired
	DBUtil DBUtil;
	
	@RequestMapping(value = "/ILDirectorAction", method = {RequestMethod.GET, RequestMethod.POST})
	public String postValues(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		if (request.getMethod().equals(RequestMethod.GET.toString())) {
			return "NotAuthorized";
		}
		// Security Check
		String environment = getEnvironment(request);
		String webAppName = request.getParameter("webAppName");

		if ("prod2".equals(environment)) {
			// iAgent attempts go to a different page/action to encrypt the
			// parameters into an XML payload which is submitted to the
			// application
			if (webAppName.equals("adm")) {
				// The actual primary_login_id of a client's SSO user is of the
				// format programcode_username
				String agentUserName = request.getParameter("clientProgramCode") + "_"
						+ request.getParameter("agentUserName");
				if (!safetyCheckiAgent(agentUserName)) {
					return "Home";
				}
			} else {
				boolean safetyCheckPassed = true;
				String userName = request.getParameter("userName");
				String accountNumber = request.getParameter("accountNumber");

				// for DFS Card SSO the parameter is accountNumber instead of
				// userName, so in that case
				// we have to do the safety check a little differently. It is
				// possible for neither to have a value (i.e. the Discover OMA
				// visitor link)
				// so only check if a value is there.
				if (StringUtils.isNotBlank(accountNumber)) {
					safetyCheckPassed = safetyCheck(webAppName, accountNumber, true);
				} else if (StringUtils.isNotBlank(userName)) {
					safetyCheckPassed = safetyCheck(webAppName, userName, false);
				}

				if (!safetyCheckPassed) {
					return "Home";
				}

			}
		}
		if (webAppName.equals("adm")) {
			// iagent goes to its own forward
			return "IAgentSSOEncryptParameters";
		} else {
			// If token is to be generated in xml format, then we use a
			// different page altogether so forward differently.
			if (StringUtils.isNotBlank(request.getParameter("tokenType"))
					&& request.getParameter("tokenType").equalsIgnoreCase("XML")) {
				return "IntegratedLoginXMLEncryptParameters";
			}
			return "IntegratedLoginEncryptParameters";
		}

	}

	String getEnvironment(HttpServletRequest request) throws ServletException {
		if (request.getServerName().equals("localhost")) {
			return "localhost";
		}

		String sql = "SELECT value env FROM APPLICATION_PROPERTIES " + "WHERE APPLICATION_PROPERTY_CLASS IN "
				+ "(SELECT ID FROM APPLICATION_PROPERTY_CLASSES "
				+ "WHERE APPLICATION IN ((SELECT ID FROM APPLICATIONS WHERE NAME = 'Common')) "
				+ "AND NAME = 'GenericProperties') " + "AND NAME = 'ENV'";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(Constants.LOLA_DS);
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			return rs.getString("env");
		} catch (Exception x) {
			throw new ServletException(x);
		} finally {
			DBUtil.close(conn, rs, stmt);
		}
	}

	boolean safetyCheck(String webAppName, String username, boolean accountNumberPassed) throws ServletException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";

		if (accountNumberPassed) {
			sql = "SELECT seed_flag FROM accounts WHERE accounts.client_program = (SELECT client_program FROM applications WHERE name = ?)  AND account_number = ? and status not in ('C','P')";
		} else {
			sql = "SELECT seed_flag FROM accounts, account_logins WHERE account_logins.account = accounts.id and account_logins.client_program = (SELECT client_program FROM applications WHERE name = ?) AND primary_login_id = ? AND (expiration_date IS NULL OR expiration_date > sysdate)";
		}

		try {
			conn = DBUtil.getConnection(Constants.LOLA_DS);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, webAppName);
			stmt.setString(2, username);
			rs = stmt.executeQuery();
			while (rs.next()) {
				return "Y".equals(rs.getString("seed_flag"));
			}
		} catch (SQLException x) {
			throw new ServletException(x);
		} finally {
			DBUtil.close(conn, rs, stmt);
		}

		return false;
	}

	boolean safetyCheckiAgent(String username) throws ServletException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";

		// ensure that in prod we can only jump in with a non-client iagent sso
		// username.
		sql = "select sit.name sit_name from account_logins al, sites sit where login_type='AS' and web_application='IG' and al.site=sit.id and al.expiration_date is null"
				+ " and primary_login_id=? and sit.name!='CLIENT'";
		try {
			conn = DBUtil.getConnection(Constants.LOLA_DS);
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			while (rs.next()) {
				return true;
			}
		} catch (SQLException x) {
			throw new ServletException(x);
		} finally {
			DBUtil.close(conn, rs, stmt);
		}

		return false;
	}
}
