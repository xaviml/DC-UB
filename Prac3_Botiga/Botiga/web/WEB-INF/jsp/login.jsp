<%-- 
    Document   : index
    Created on : 08-may-2014, 1:17:32
    Author     : zenbook
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>login</h1>
	<%
	if(request.getParameter("error") != null) {
	if (request.getParameter("error").equals("true")) {
	%>
	    ERROR
	<%} else{%>
	<%}}%>

	<form method='POST' action='<%= response.encodeURL("j_security_check") %>'>
	    Usuari:         <input type='text'     name='j_username'>
	    <br>
	    Paraula de pas: <input type='password' name='j_password'>
	    <br>
	    <input type="submit" value="Enviar">
	</form>	

    </body>
</html>
