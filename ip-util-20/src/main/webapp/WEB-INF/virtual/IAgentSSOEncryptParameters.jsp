<jsp:useBean id="iagentSSOEncryptBean" scope="request" class="com.loyalty.common.ilclient.IAgentSSOEncryptBean"></jsp:useBean>
<jsp:setProperty name="iagentSSOEncryptBean" property="*"/>

<% iagentSSOEncryptBean.setRequest(request); %>
<% iagentSSOEncryptBean.setResponse(response); %>

<%-- Perform processing associated with the JSP --%>
<% iagentSSOEncryptBean.process(); %>

<%-- If we got this far, an error happened --%>
An error happened.