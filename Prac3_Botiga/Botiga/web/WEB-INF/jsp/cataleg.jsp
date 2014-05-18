<%-- 
    Document   : index
    Created on : 08-may-2014, 1:17:32
    Author     : zenbook
--%>

<%@page import="java.util.HashMap"%>
<%@page import="ub.botiga.data.Product"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="static/css/generic.css" type="text/css" rel="stylesheet"/>
	<link href="static/css/cataleg.css" type="text/css" rel="stylesheet"/>
        <title>JSP Page</title>
    </head>
    <body>
	<%@include file="header.jsp" %>
	<div id="body-content">
	    <form action="/Botiga/Compra" method="POST">
		<input id="submit" type="submit" class="button" value="Compra!"/>
		<div id="content-cataleg">
		    <h1 id="musica" class="title">Música</h1>
		    <hr/>
		    <div id="musicadiv" class="prod-container">
			<br/>
			<c:forEach var="prod" items="${mp3}">
			    <div class="row">
				<label for="${prod.name}" class="checkbox">
				    <input  type="checkbox" class="nomprod" name="${prod.name}" id="${prod.name}"/>
				    ${prod.name}
				<p class="element">${prod.description}</p>
				<p class="element">${prod.price} €</p>
				</label> 
			    </div>
			</c:forEach>
			
		    </div>

		    <h1 id="llibres" class="title">Llibres</h1>
		    <hr/>
		    <div id="llibresdiv" class="prod-container">
			<br/>
			<c:forEach var="prod" items="${llibres}">
			    <div class="row">
				<label for="${prod.name}" class="checkbox">
				    <input  type="checkbox" class="nomprod" name="${prod.name}" id="${prod.name}"/>
				    ${prod.name}
				<p class="element">${prod.description}</p>
				<p class="element">${prod.price} €</p>
				</label> 
			    </div>
			</c:forEach>
		    </div>

		    <h1 id="videos" class="title">Vídeos</h1>
		    <hr/>
		    <div id="videosdiv" class="prod-container">
			<br/>
			<c:forEach var="prod" items="${videos}">
			    <div class="row">
				<label for="${prod.name}" class="checkbox">
				    <input  type="checkbox" class="nomprod" name="${prod.name}" id="${prod.name}"/>
				    ${prod.name}
				<p class="element">${prod.description}</p>
				<p class="element">${prod.price} €</p>
				</label> 
			    </div>
			</c:forEach>
		    </div>
		</div>
	    </form>
	</div>
    </body>
</html>
