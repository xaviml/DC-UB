<%-- 
    Document   : index
    Created on : 08-may-2014, 1:17:32
    Author     : zenbook
--%>

<%@page import="ub.botiga.data.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession s = request.getSession();
    User u = null;
    if(s.getAttribute("user") != null) {
	u = (User) s.getAttribute("user");
    }

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
	<div id="header">
	    <div id="header-content">
		<a href="/Botiga/"><h1 id="title">BotigaOnline</h1></a>
		<% if(u == null) { %>
		    <!-- Si l'usuari no està en el sistema: -->
		    <div id="login" class="right-header">
			<a id="cataleg"  class="left-content link" href="Cataleg">Catàleg</a>
			<form action="login" method="GET" class="left-content">
			    <input type="submit" id="authtent"  value="Log in" class="button"/>
			</form>
		    </div>
		<%}else{%>
		    <!-- Si l'usuari està en el sistema -->
		    <div id="seccio-registrat" class="right-header">
			<p id="benvolgut" class="left-content">Hola, <%=u.getName()%></p>
			<p class="left-content" style="color: #c8d7a4;">|</p>
			
			<p id="credits" class="left-content"><%=u.getCredits()%>  €</p>
			<div id="seccio-registrat-avall">
			    <p class="left-content"> <a id="cataleg" class="link" href="/Botiga/Cataleg">Catàleg</a></p>
			    <p class="left-content" style="color: #c8d7a4;">|</p>
			    
			    <p class="left-content"> <a id="historial"  class="link" href="/Botiga/Historial">Historial</a></p>
			    <p class="left-content" style="color: #c8d7a4;">|</p>

			    <p class="left-content"> <a id="cistell"  class="link" href="/Botiga/Cistell">Cistell</a></p>
			    <p class="left-content" style="color: #c8d7a4;">|</p>

			    <p class="left-content"> <a id="logout"  class="link" href="/Botiga/logout">Sortir</a></p>
			</div>
		    </div>
		<%}%>
		
	    </div>
	</div>
    </body>
</html>
