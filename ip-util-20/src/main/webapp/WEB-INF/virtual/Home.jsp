<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="-1" />
<title>Integrated Login Jump page(s)</title>
<link href="css/styles.css?ver=1" rel="stylesheet" type="text/css"
	media="all">
<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />
</head>

<body>
	<%
		String env = getEnvironment(request);
		// env = "devl";
		// env = "projtest";
		// env = "qa";
		// env = "staging";
		// env = "prod";
		boolean passiveMode = false;
		boolean showHWLinks = true;
		if ("TRUE".equalsIgnoreCase(System.getProperty("cxl.passive"))) {
			passiveMode = true;
		}

		if ("prod".equalsIgnoreCase(env) && !passiveMode) {
			showHWLinks = false;
		}

		request.setAttribute("env", env);

		out.println("<!-- env: " + env + " -->");

		pageContext.setAttribute("env", env);

	%>
	
	<table width="50%" border="0">
		<tr>
			<td><b>Integrated Login Jump Page Link(s)</b> <html:errors /></td>
		</tr>

		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>BB&amp;T</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILBBTAdminUsers" value="true" /> <% } %> <c:import
					url="IntegratedLoginInclude.jsp">
					<c:param name="_webAppName" value="ip-bbt" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILBBTAdminUsers}" />
				</c:import>

			</td>
		</tr>
		<% } %>

		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Commerce Bank of Kansas (CBKC)</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILCBKCAdminUsers" value="true" /> <% } %> <c:import
					url="CommerceBankSsoInclude.jsp">
					<c:param name="_webAppName" value="ip-cbkc" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILCBKCAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>
		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Bank Fund Staff (BFS)</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILBFSAdminUsers" value="true" /> <% } %> <c:import
					url="BankFundSsoInclude.jsp">
					<c:param name="_webAppName" value="ip-bfs" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILBFSAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>
		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>MYCOMPANY Mobile (MOMA)</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILMyCompanyAdminUsers" value="true" /> <% } %> <c:import
					url="MycoMobileSsoInclude.jsp">
					<c:param name="_webAppName" value="mo-mc" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILMyCompanyAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>

		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Delta</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILDECUAdminUsers" value="true" /> <% } %> <c:import
					url="IntegratedLoginInclude.jsp">
					<c:param name="_webAppName" value="ip-decu" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILDECUAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>

		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Dreyfus - Baird</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILBairdAdminUsers" value="true" /> <% } %> <c:import
					url="IntegratedLoginInclude.jsp">
					<c:param name="_webAppName" value="ip-d4-baird" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILBairdAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>
		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Dreyfus - Janney</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILJanneyAdminUsers" value="true" /> <% } %> <c:import
					url="IntegratedLoginInclude.jsp">
					<c:param name="_webAppName" value="ip-d4-janney" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILJanneyAdminUsers}" />
				</c:import> <c:import url="IntegratedLoginInclude.jsp">
					<c:param name="_webAppName" value="ip-jr" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILJanneyAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>

		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Dreyfus - Stifel</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILStifelAdminUsers" value="true" /> <% } %> <c:import
					url="IntegratedLoginInclude.jsp">
					<c:param name="_webAppName" value="ip-d4-stifel" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILStifelAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>

		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Sovereign Bank</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILSBRAdminUsers" value="true" /> <% } %> <c:import
					url="IntegratedLoginInclude.jsp">
					<c:param name="_webAppName" value="ip-sbr" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILSBRAdminUsers}" />
				</c:import>
			</td>
		</tr>
		<% } %>


		<% if (request.isUserInRole("CC_FINUser")) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Wells Fargo</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILWellsFargoAdminUsers" value="true" /> <% } %> <c:import
					url="WellsFargoSsoInclude.jsp">
					<c:param name="_webAppName" value="ip-wf" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILWellsFargoAdminUsers}" />
				</c:import>

			</td>
		</tr>
		<% } %>

		<% if (request.isUserInRole("CC_FINUser") && showHWLinks) { %>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b><u>Health &amp; Wellness</u></b></td>
		</tr>
		<tr>
			<td>
				<% if (request.isUserInRole("CC_FINUser")) { %> <c:set
					var="ILHealthWellnessAdminUsers" value="true" /> <% } %> <c:import
					url="HealthWellnessSsoInclude.jsp">
					<c:param name="_webAppName" value="hw-jtwb" />
					<c:param name="_environment" value="${env}" />
					<c:param name="_admin" value="${ILHealthWellnessAdminUsers}" />
				</c:import>

			</td>
		</tr>
		<% } %>


	</table>


	<hr noshade width="500" align="left" size="1">
	&raquo;
	<a href="<%=request.getContextPath()%>/logout">Logout</a>
</body>
</html>

<%!//Get the environment from the system properties.
	String getEnvironment(HttpServletRequest request) throws Exception {
		return System.getProperty("cxl.environment");
	}%>