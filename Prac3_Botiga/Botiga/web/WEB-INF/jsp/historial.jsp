<%-- 
    Document   : index
    Created on : 08-may-2014, 1:17:32
    Author     : zenbook
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="static/css/generic.css" type="text/css" rel="stylesheet"/>
	<link href="static/css/historial.css" type="text/css" rel="stylesheet">
	
        <title>JSP Page</title>
    </head>
    <body>
	<%@include file="header.jsp" %>
	<div id="body-content">
	    <h1 class="title">Historial</h1>
	    <hr/>
	    <br/>
	    <c:forEach var="prod" items="${historial}">
		<div class="row">
		    <p class="nomprod">${prod.name}</p>
		    <p class="price">${prod.description}</p>
		    <a href="/Botiga/Producte/${prod.name}"><button class="button" >Descarrega aquí!</button></a>
		</div>
	    </c:forEach>
	    <br/>
	</div>
    </body>
</html>
