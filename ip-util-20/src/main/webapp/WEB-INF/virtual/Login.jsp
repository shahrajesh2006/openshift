<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="-1" />
<title>Get Authorized</title>
<link href="css/styles.css?ver=1" rel="stylesheet" type="text/css"
	media="all">
</head>
<body>
	<%@ include file="FailWidget.jsp"%>
	<br>
	<br>
	<form action="login" method="post" style="margin: 0px;">
		<table border="0" cellpadding="1" cellspacing="1">
			<tr>
				<td align="right">User Name:</td>
				<td><input type="text" name="username" id="username"
					class="inputtext" size="20" title="User Name" value=""
					autocomplete="off" /></td>
			</tr>
			<tr>
				<td align="right">Password:</td>
				<td><input type="password" name="password" id="password"
					class="inputtext" size="20" title="Password" value=""
					autocomplete="off" /></td>
			</tr>
			<tr>
				<td colspan="2" align="right"><input type="submit"
					value="Login"></td>
			</tr>
		</table>
	</form>
</body>
</html>
