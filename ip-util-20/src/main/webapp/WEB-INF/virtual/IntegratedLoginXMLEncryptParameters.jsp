<jsp:useBean id="integratedLoginXMLEncryptBean" scope="request" class="com.loyalty.common.ilclient.IntegratedLoginXMLAESEncryptBean"></jsp:useBean>
<jsp:setProperty name="integratedLoginXMLEncryptBean" property="*"/>

<% integratedLoginXMLEncryptBean.setRequest(request); %>
<% integratedLoginXMLEncryptBean.setResponse(response); %>

<%-- Perform processing associated with the JSP --%>
<% integratedLoginXMLEncryptBean.process(); %>

<%-- If we got this far, an error happened --%>
An error happened.