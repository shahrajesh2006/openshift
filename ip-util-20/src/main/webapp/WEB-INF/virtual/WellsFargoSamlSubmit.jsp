<html>
	<head>
     	<script type="text/javascript">
     		function load()
     		{
          		window.document.test.submit();
          		return;
     		}
    	 </script>
	</head>
	<body onload="load()">
		<form name="test" method="post" action="${ssoUrl}">
    		<input type="hidden" name="SAMLResponse" value="${samlToken}" />
		</form>
	</body>
</html>
