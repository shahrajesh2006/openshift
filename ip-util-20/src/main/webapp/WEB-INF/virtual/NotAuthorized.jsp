<html>
	<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache" />
    <meta http-equiv="cache-control" content="no-cache" />
    <meta http-equiv="expires" content="-1" />
		<title>Not Authorized</title>
		<link href="css/styles.css?ver=1" rel="stylesheet" type="text/css" media="all">
	</head>
	<body>
		<div>
			<img src="images/not_auth.jpg" align="left">
			<br>
			<br>
			<br>
			We are sorry. You either typed an incorrect username/password or you do not have access to the page requested.
			<br>
			<br clear="all">
			<%@ include file="FailWidget.jsp"%>
			<br>
			<br>
			Try
			<a href="<%=request.getContextPath()%>">again</a>?
		</div>
	</body>
</html>
