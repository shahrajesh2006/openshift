<%@page import="com.loyalty.common.ilclient.IntegratedLoginConstants"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<HTML>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="-1" />
<title>Transfering to <%=request.getAttribute(IntegratedLoginConstants.TEMP_J_PASSWORD_PARAMETER_NAME)%></title>
<script>
function handle(form) {
	if (form.elements['openInNewWindow'].checked) {
		form.target = '_blank';
	} else {
		form.target = '_self';
	}

	return true;
}
</script>
</head>
<BODY>

<form name="form" action="<%=request.getParameter(IntegratedLoginConstants.J_SECURITY_CHECK_PARAMETER_NAME)%>" method="post" onsubmit="handle(this)"
  <c:if test="${param.openInNewWindow}">
  		<c:set var="checked" value="checked='checked'"/>
  		<c:out value="target='_blank'" escapeXml="false"/>
  </c:if>
>
  <h1>Hit Go below to access <%=request.getAttribute(IntegratedLoginConstants.TEMP_J_PASSWORD_PARAMETER_NAME)%></h1><HR>
  This form is submitting to: <%=request.getParameter(IntegratedLoginConstants.J_SECURITY_CHECK_PARAMETER_NAME)%><BR>
  <input type="hidden" name="<%=IntegratedLoginConstants.J_USERNAME_PARMETER_NAME%>" value="<%=request.getAttribute(IntegratedLoginConstants.TEMP_J_USERNAME_PARAMETER_NAME)%>" size="100">
  <input type="hidden" name="<%=IntegratedLoginConstants.J_PASSWORD_PARMETER_NAME%>" value="<%=request.getAttribute(IntegratedLoginConstants.TEMP_J_PASSWORD_PARAMETER_NAME)%>" size="100">
  <input type="hidden" name="<%=IntegratedLoginConstants.AUTH_URL_PARAMETER_NAME%>" value="<%=request.getAttribute(IntegratedLoginConstants.AUTH_URL_PARAMETER_NAME)%>" size="100">
  <input type="hidden" name="<%=IntegratedLoginConstants.LOGOUT_URL_PARAMETER_NAME%>" value="<%=request.getAttribute(IntegratedLoginConstants.LOGOUT_URL_PARAMETER_NAME)%>" size="100">
  <input type="hidden" name="<%=IntegratedLoginConstants.PING_URL_PARAMETER_NAME%>" value="<%=request.getAttribute(IntegratedLoginConstants.PING_URL_PARAMETER_NAME)%>" size="100">

<%if(request.getAttribute(IntegratedLoginConstants.EMAIL_CAMPAIGN)!= null){ %>  
  <input type="hidden" name="<%=IntegratedLoginConstants.EMAIL_CAMPAIGN%>" value="<%=request.getAttribute(IntegratedLoginConstants.EMAIL_CAMPAIGN)%>" size="100">
  <%} %>
<%if(request.getAttribute(IntegratedLoginConstants.REDIRECT_TO)!= null){ %>  
  <input type="hidden" name="<%=IntegratedLoginConstants.REDIRECT_TO%>" value="<%=request.getAttribute(IntegratedLoginConstants.REDIRECT_TO)%>" size="100">
<%}%>
  
  <input type="checkbox" name="openInNewWindow" id="openInNewWindow" <c:out value="${checked}" escapeXml="false"/>> <label for="openInNewWindow">Open in a new window?</label> <input type="submit" name="Submit" value="Go">
</form>
</BODY>
</HTML>