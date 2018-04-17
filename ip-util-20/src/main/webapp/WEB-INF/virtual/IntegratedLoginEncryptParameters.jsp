<jsp:useBean id="integratedLoginEncryptBean" scope="request" class="com.loyalty.common.ilclient.IntegratedLoginEncryptBean"></jsp:useBean>
<jsp:setProperty name="integratedLoginEncryptBean" property="*"/>

<% integratedLoginEncryptBean.setRequest(request); %>
<% integratedLoginEncryptBean.setResponse(response); %>

<%-- Perform processing associated with the JSP --%>
<% integratedLoginEncryptBean.process(); %>

<%-- If we got this far, an error happened --%>
An error happened.