/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.botiga;

import ub.botiga.data.Data;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ub.botiga.data.User;


/**
 *
 * @author zenbook
 */
public class ServletDispatcher extends HttpServlet {

    private Data data;
    
    @Override
    public void init() throws ServletException {
	super.init();
	ServletContext c = getServletContext();
	String users = c.getRealPath("WEB-INF/users.json");
	String products = c.getRealPath("WEB-INF/products.json");

	data = new Data(users, products);
    }
    
    private void locationProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String CONTEXT = request.getContextPath();
	String location = request.getRequestURI();
	if(location.equals( CONTEXT + "/")){
	    showPage( request, response, "index.jsp");

	} else if ( location.equals( CONTEXT + "/login") ) { 
	    showPage( request, response, "authtent.jsp");
	    
	} else if(location.equals( CONTEXT + "/logout") ){ 
	    request.getSession().invalidate();
	    response.sendRedirect(CONTEXT + "/");
	    //showPage( request, response, "index.jsp");
	    
	} else if(location.equals( CONTEXT + "/Cataleg") ){ 
	    showPage( request, response, "cataleg.jsp");
	    
	} else if(location.equals( CONTEXT + "/Cistell") ){ 
	    showPage( request, response, "cistell.jsp");
	    
	} else if(location.equals( CONTEXT + "/Historial") ){ 
	    showPage( request, response, "historial.jsp");
	    
	} else{
	    showPage( request, response, "error404.jsp");
	}
}
    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
	controlUser(request, response);
	locationProxy(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	//Implement POST-Redirect-GET Pattern Design.
	String parameters = getParameters(request);
	response.sendRedirect(request.getRequestURI()+parameters);
    }

    public void showPage(HttpServletRequest request, HttpServletResponse response, String jspPage) throws ServletException, IOException {
	//String parameters = getParameters(request);
 	RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/jsp/" + jspPage);
	rd.forward(request, response);
    }
    
    private String getParameters(HttpServletRequest request) {
	String q = request.getQueryString();
	return q != null ? "?"+q : "";
    }

    private void controlUser(HttpServletRequest request, HttpServletResponse response) {
	String user = request.getRemoteUser();
	if(user != null) {
	    User u = data.addUser(user);
	    HttpSession s = request.getSession();
	    s.setAttribute("user", u);
	}
    }
}