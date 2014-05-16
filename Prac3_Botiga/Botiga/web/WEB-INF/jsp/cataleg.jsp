<%-- 
    Document   : index
    Created on : 08-may-2014, 1:17:32
    Author     : zenbook
--%>

<%@page import="ub.botiga.data.Product"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="static/css/generic.css" type="text/css" rel="stylesheet"/>
	<link href="static/css/cataleg.css" type="text/css" rel="stylesheet">
	
	
        <title>JSP Page</title>
    </head>
    <body>
	<%@include file="header.jsp" %>
	<div id="body-content">
	    <!--
	    <div id="menu" class="background-box">
		<h1 id="index" class="title">Productes</h1>
		<p class="itemindex"><a class="link" href="/Botiga/Cataleg#musica">Música</a></p>
		<p class="itemindex"><a class="link" href="/Botiga/Cataleg#llibres">Llibres</a></p>
		<p class="itemindex"><a class="link" href="/Botiga/Cataleg#videos">Vídeos</a></p>
	    </div>-->
	    <form action="Compra" method="POST">
		<input id="submit" type="submit" class="button" value="Compra!"/>
		<div id="content-cataleg">
		    <h1 id="musica" class="title">Música</h1>
		    <hr/>
		    <div id="musicadiv" class="prod-container">
			<br/>
			<c:forEach var="prod" items="${mp3}">
			    <div class="row">
				<label for="${prod.name}" class="nomprod checkbox"><input type="checkbox" class="nomprod" name="${prod.name}" id="${prod.name}"/>${prod.name}</label> 
				<p class="price">${prod.price}</p>
			    </div>
			</c:forEach>
			
		    </div>

		    <h1 id="llibres" class="title">Llibres</h1>
		    <hr/>
		    <div id="llibresdiv" class="prod-container">
			<c:forEach var="prod" items="${llibres}">
			    <div class="row">
				<label for="${prod.name}" class="nomprod checkbox"><input type="checkbox" class="nomprod" name="${prod.name}" id="${prod.name}"/>${prod.name}</label> 
			    </div>
			</c:forEach>
		    </div>

		    <h1 id="videos" class="title">Vídeos</h1>
		    <hr/>
		    <div id="videosdiv" class="prod-container">
			<c:forEach var="prod" items="${videos}">
			    <div class="row">
				<label for="${prod.name}" class="nomprod checkbox"><input type="checkbox" class="nomprod" name="${prod.name}" id="${prod.name}"/>${prod.name}</label> 
			    </div>
			</c:forEach>
		    </div>
		</div>
	    </form>
	</div>
    </body>
</html>
