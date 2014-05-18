<%-- 
    Document   : index
    Created on : 08-may-2014, 1:17:32
    Author     : zenbook
--%>

<%@page import="java.util.Collection"%>
<%@page import="ub.botiga.data.Product"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="static/css/generic.css" type="text/css" rel="stylesheet"/>
	<link href="static/css/cistell.css" type="text/css" rel="stylesheet">
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="header.jsp" %>
	<div id="body-content">
	    <h1 class="title">Cistell</h1>
	    <hr/>
	    <br/>
	    <c:forEach var="prod" items="${cistell}">
		<div class="row">
		    <p class="element nomprod">${prod.name}</p>
		    <p class="element">${prod.description}</p>
		    <p class="element">${prod.price} €</p>
		</div>
	    </c:forEach>
	    
	    <% Collection<Product> cistell = (Collection<Product>) request.getAttribute("cistell");
	    if(cistell.size() > 0) {%>
		<br/>
		<h1 class="title">Confirmació de la compra</h1>
		<hr/>
		<br/>
		<p id="preu">Total: <c:out value="${preucistell}"/> €</p>
		<form action="/Botiga/Confirma" method="POST">
		    <input id="confirma" type="submit" class="button" value="Confirma compra"/>
		</form>
		<form action="/Botiga/Cancela" method="POST">
		    <input id="cancela" type="submit" class="button" value="Cancel·la compra"/>
		</form>
	    <%} else{%>
		<br/>
		<p id="nocistell" class ="title">No hi han articles al cistell</p>
	    <%}%>
	    
	    
	</div>
    </body>
</html>
