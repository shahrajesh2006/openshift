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
		<!-- not needed anyway? -->
			<!--<input type="hidden" name="RelayState" value="${relayState}" />-->
    		<input type="hidden" name="SAMLResponse" value="${samlToken}" />
		</form>
	</body>
</html>
