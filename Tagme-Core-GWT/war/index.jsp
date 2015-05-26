<!doctype html>
<html>
	<%
		boolean isJiva = request.getServerName().contains("jiva");
		//isJiva = true;
		String title = "Tagme";
		String appProfile = "sankhya";
		String keywords = "sankhya place, sankhya, appstore, aplicativos erp, erp, plataforma colaborativa, place, solucoes corporativas, aplicativos corporativos"; 
		String sufix = "";
		String description = "A próxima revolução no mercado de ERP começa aqui! Conheça a Sankhya Place. Ambiente colaborativo e de compartilhamento de conhecimento onde clientes e consultores usufruem de aplicativos e conteúdos ligados ao ecossistema do ERP Sankhya.";
		
		if(isJiva){
			title = "Tag Me";
			appProfile = "jiva";
			keywords = "jiva place, jiva, appstore, aplicativos erp, erp, plataforma colaborativa, place, solucoes corporativas, aplicativos corporativos";
			sufix = "-jiva";
			description = "A próxima revolução no mercado de ERP começa aqui! Conheça o Jiva Place. Ambiente colaborativo e de compartilhamento de conhecimento onde clientes e consultores usufruem de aplicativos e conteúdos ligados ao ecossistema do ERP Jiva.";
		}

	%>
  <head>
  	<title><%=title%></title>
  	<script type="text/javascript">
  		var appProfile = "<%=appProfile%>";
  	</script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    
    <link rel="shortcut icon" href="gwt/favicon<%=sufix%>.ico" />
	<meta name="keywords" content="<%=keywords%>" />
	<meta name="robots" content="index, follow" />
	<meta name="googlebot" content="index, follow" />
	<meta name="description" content="<%=description%>" />
    <script type="text/javascript" src="gwt/js/pace.min.js"></script>
    <link rel="stylesheet" href="gwt/css/bootstrap<%=sufix%>.min.css" type="text/css"/>
    <link rel="stylesheet" href="gwt/css/font-awesome-4.3.0.min.cache.css" type="text/css"/>
    <link rel="stylesheet" href="gwt/MediaStyles<%=sufix%>.css" type="text/css"/>  
    <script type="text/javascript" language="javascript" src="gwt/gwt.nocache.js"></script>
    <script type="text/javascript">
    	paceOptions = {
    			restartOnPushState: true
    	}
    </script>
  </head>

  <body>

  </body>
</html>
