/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.botiga;

import ub.botiga.data.Data;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
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
	data = new Data();
    }
    
    private void locationProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String CONTEXT = request.getContextPath();
	String location = request.getRequestURI();
	System.out.println(CONTEXT +" - " + location);
	if(location.equals( CONTEXT + "/BO/")){
	    showPage( request, response, "index.jsp");
	}else if ( location.equals( CONTEXT + "/BO/login") ) { 
	    showPage( request, response, "login.jsp");
	}else if(location.equals( CONTEXT + "/BO/Cataleg") ){ 
	    showPage( request, response, "cataleg.jsp");
	}else if(location.equals( CONTEXT + "/BO/Cistell") ){ 
	    showPage( request, response, "cistell.jsp");
	} else if(location.equals( CONTEXT + "/BO/Historial") ){ 
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

	/*String user = req.getParameter("j_username");
	String pass = req.getParameter("j_password");

	response.sendRedirect("/Project-web/"+request.);*/
	doGet(request, response);
    }

    public void showPage(HttpServletRequest request, HttpServletResponse response, String jspPage) throws ServletException, IOException {
	RequestDispatcher rd = request.getRequestDispatcher( "/WEB-INF/jsp/" + jspPage);
	rd.forward(request, response);
	//El que hi ha aquí no s'executarà mai
    }

    private void controlUser(HttpServletRequest request, HttpServletResponse response) {
	String user = request.getRemoteUser();
	if(user != null) {
	    User u = data.addUser(user);
	    HttpSession s = request.getSession();
	    s.setAttribute(u.getName(), u);
	}
    }
}