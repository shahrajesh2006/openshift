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
  <input type="hidden" name="payload" value="<%=request.getAttribute(IntegratedLoginConstants.TEMP_J_USERNAME_PARAMETER_NAME)%>">
  <input type="checkbox" name="openInNewWindow" id="openInNewWindow" <c:out value="${checked}" escapeXml="false"/>> <label for="openInNewWindow">Open in a new window?</label> <input type="submit" name="Submit" value="Go">
</form>
</BODY>
</HTML>