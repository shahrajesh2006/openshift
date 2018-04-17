<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.List"%>
<%@page import="com.loyalty.common.ilclient.ConfigReader"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%
	String webAppName = request.getParameter("_webAppName");
	//Temp code - to be deleted later
	out.println("<!-- webAppName - " + webAppName + " -->");
	String environment = request.getParameter("_environment");
	out.println("<!-- environment - " + environment + " -->");
	boolean isAdmin = Boolean.valueOf(request.getParameter("_admin")).booleanValue();
	out.println("<!-- isAdmin - " + isAdmin + " -->");

	ConfigReader configReader = new ConfigReader();
	out.println("<!-- configReader - " + configReader + " -->");

	List objects = configReader.getOMAURLObjects(webAppName, environment);
	//Add iagent objects, since they use this same include since they write out this form the same
	objects.addAll(configReader.getIAgentURLObjects(webAppName, environment));
	out.println("<!-- objects - " + objects + " -->");
	out.println("<!-- objects.size() - " + objects.size() + " -->");

	for (Iterator i = objects.iterator(); i.hasNext();) {
		ConfigReader.URLObject urlObject = (ConfigReader.URLObject) i.next();
		out.println("<!-- urlObject - " + urlObject + " -->");
		out.println("<!-- urlObject.hashCode() - " + urlObject.hashCode() + " -->");
%>

<form:form action="MYCOMobileSsoAction" method="post" style="margin:0px;"
	id='<%=("f_" + urlObject.hashCode())%>'
	styleId='<%=("f_" + urlObject.hashCode())%>'>
	<input type="hidden" property="method" value="postValues" />

	<%
		for (Iterator ii = urlObject.parameters.entrySet().iterator(); ii.hasNext();) {
					Map.Entry e = (Map.Entry) ii.next();
					out.println("<input type='hidden' name='" + e.getKey() + "' value='" + e.getValue() + "'>");
					out.println("<!-- e.getKey() - " + e.getKey() + " -->");
					out.println("<!-- e.getValue() - " + e.getValue() + " -->");
				}
				out.println("<!-- urlObject.display - " + urlObject.display + " -->");
	%>
	<%
		//Default to display the link mouseover text as primary login id, for OMA
				String title = "Account Number: " + urlObject.parameters.get("accountNumber");
	%>
	<a href="#"
		onclick="document.forms['f_<%=urlObject.hashCode()%>'].submit()"
		title="<%=title%>"><%=urlObject.display%></a>

	<%
		if (isAdmin) {
	%>

	| <a href="#"
		onclick="document.getElementById('d_<%=urlObject.hashCode()%>').style.display = 'inline';">change?</a>

	<%
		}
	%>

</form:form>

<%
	if (isAdmin) {
%>

<div id="d_<%=urlObject.hashCode()%>" style="display: none;">

	<form:form action="MYCOMobileSsoAction"  method="post" style="margin:0px;">
		<input type="hidden" property="method" value="postValues" />

		<table border="0"
			style="margin: 4px; border-width: 1px; border-color: gray; border-style: solid;">

			<tr>
				<td colspan="2">You can change any of the values below.</td>
			</tr>

			<%
				for (Iterator ii = urlObject.parameters.entrySet().iterator(); ii.hasNext();) {
								out.println("<tr>");

								Map.Entry e = (Map.Entry) ii.next();

								out.println("<td align=right>" + e.getKey() + ":</td>");

								out.println("<td><input type='text' name='" + e.getKey() + "' value='" + e.getValue()
										+ "' size='70'></td>");

								out.println("<!-- e.getKey() - " + e.getKey() + " -->");
								out.println("<!-- e.getValue() - " + e.getValue() + " -->");

								out.println("</td></tr>");
							}
			%>

			<tr>
				<td colspan="2" align="right"><input type="submit"
					value="Submit"></td>
				<td><input type="button"
					onclick="document.getElementById('d_<%=urlObject.hashCode()%>').style.display = 'none';"
					value="Close"></td>
			</tr>

		</table>

	</form:form>

</div>
<% } %>


<% } %>


