/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ub.botiga;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import ub.botiga.data.Data;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.catalina.startup.PasswdUserDatabase;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.dbcp.pool.impl.GenericKeyedObjectPool;
import org.json.JSONArray;
import ub.botiga.data.Product;
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
	if (location.equals(CONTEXT + "/")) {
	    showPage(request, response, "index.jsp");

	} else if (location.equals(CONTEXT + "/login")) {
	    showPage(request, response, "authtent.jsp");

	} else if (location.equals(CONTEXT + "/logout")) {
	    request.getSession().invalidate();
	    response.sendRedirect(CONTEXT + "/");

	} else if (location.equals(CONTEXT + "/Cataleg")) {
	    showCataleg(request, response);

	} else if (location.equals(CONTEXT + "/Cistell")) {
	    showCistell(request, response);

	} else if (location.equals(CONTEXT + "/Historial")) {
	    showHistorial(request, response);

	} else if (location.contains(CONTEXT + "/Producte")) {
	    controlProduct(request, response);

	} else if (location.contains(CONTEXT + "/augsaldo")) {
	    controlWebServices(request, response);

	} else {
	    showPage(request, response, "error404.jsp");
	}
    }

    private void controlPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	String CONTEXT = request.getContextPath();
	String location = request.getRequestURI();
	if (location.equals(CONTEXT + "/Compra")) {
	    comprar(request, response);
	} else if (location.equals(CONTEXT + "/Cancela")) {
	    cancela(request, response);
	} else if (location.equals(CONTEXT + "/Confirma")) {
	    confirma(request, response);
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
	controlPost(request, response);
    }

    private void showCistell(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	HttpSession session = request.getSession();
	User u = (User) session.getAttribute("user");
	if (u == null) {
	    response.sendRedirect("Botiga/");
	    return;
	}

	HashMap<String, Product> cistell = getCistell(request);
	HashMap<String, Product> historial = u.getProducts();
	for (Product p : historial.values()) {
	    cistell.remove(p.getName());
	}
	session.setAttribute("cistell", cistell);

	request.setAttribute("cistell", cistell.values());
	DecimalFormat df = new DecimalFormat();
	df.setMaximumFractionDigits(2);
	DecimalFormatSymbols dfs = new DecimalFormatSymbols();
	dfs.setDecimalSeparator('.');

	df.setDecimalFormatSymbols(dfs);
	request.setAttribute("preucistell", df.format(getPreuCistell(request)));
	showPage(request, response, "cistell.jsp");
    }

    private void showCataleg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	HashMap<String, Product> cistell = getCistell(request);

	ArrayList<Product> mp3 = new ArrayList<Product>();
	ArrayList<Product> videos = new ArrayList<Product>();
	ArrayList<Product> llibres = new ArrayList<Product>();
	for (Product product : data.getProductes().values()) {
	    if (product.getType() == Product.FileType.MP3 && !cistell.containsKey(product.getName())) {
		mp3.add(product);
	    } else if (product.getType() == Product.FileType.PDF && !cistell.containsKey(product.getName())) {
		llibres.add(product);
	    } else if (product.getType() == Product.FileType.VIDEO && !cistell.containsKey(product.getName())) {
		videos.add(product);
	    }
	}

	request.setAttribute("mp3", mp3);
	request.setAttribute("llibres", llibres);
	request.setAttribute("videos", videos);

	showPage(request, response, "cataleg.jsp");
    }

    private void showHistorial(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	User u = (User) request.getSession().getAttribute("user");
	if (u == null) {
	    response.sendRedirect("Botiga/");
	    return;
	}
	HashMap<String, Product> historial = u.getProducts();
	request.setAttribute("historial", historial.values());

	showPage(request, response, "historial.jsp");
    }

    public void showPage(HttpServletRequest request, HttpServletResponse response, String jspPage) throws ServletException, IOException {
	//String parameters = getParameters(request);
	RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/" + jspPage);
	rd.forward(request, response);
    }

    private void controlUser(HttpServletRequest request, HttpServletResponse response) {
	String user = request.getRemoteUser();
	if (user != null) {
	    User u = data.addUser(user);
	    HttpSession s = request.getSession();
	    s.setAttribute("user", u);
	}
    }

    private HashMap<String, Product> getCistell(HttpServletRequest request) {
	HashMap<String, Product> cistell = (HashMap<String, Product>) request.getSession().getAttribute("cistell");
	if (cistell == null) {
	    cistell = new HashMap<String, Product>();
	}
	return cistell;
    }

    private HashMap<String, Product> getProductsSelected(HttpServletRequest request) {
	HashMap<String, Product> products = getCistell(request);
	Enumeration<String> par = request.getParameterNames();
	while (par.hasMoreElements()) {
	    String prod = par.nextElement();
	    products.put(prod, data.getProductes().get(prod));
	}
	return products;
    }

    private void comprar(HttpServletRequest request, HttpServletResponse response) throws IOException {
	HttpSession session = request.getSession();
	HashMap<String, Product> cistell = getProductsSelected(request);

	/*Guardem el cistell en la sessió*/
	session.setAttribute("cistell", cistell);

	/*Redireccionem a cistell*/
	response.sendRedirect("Cistell");
    }

    private void cancela(HttpServletRequest request, HttpServletResponse response) throws IOException {
	request.getSession().removeAttribute("cistell");
	response.sendRedirect("Cataleg");
    }

    private void confirma(HttpServletRequest request, HttpServletResponse response) throws IOException {
	//Productes per comprar
	HashMap<String, Product> productes = getCistell(request);
	User u = (User) request.getSession().getAttribute("user");
	float preu = getPreuCistell(request);
	if (u == null) {
	    response.sendRedirect("Botiga/");
	    return;
	}
	if (u.getCredits() - preu < 0) { //No pot comprar
	    //TODO: do something
	} else {
	    //Borrem el cistell
	    request.getSession().removeAttribute("cistell");

	    data.buyProduct(u, productes, preu);
	    response.sendRedirect("Historial");
	}

    }

    private float getPreuCistell(HttpServletRequest request) {
	float preu = 0;
	HashMap<String, Product> cistell = getCistell(request);
	for (Product p : cistell.values()) {
	    preu += p.getPrice();
	}
	return preu;
    }

    private void controlProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	String location = request.getRequestURI();
	String prod = URLDecoder.decode(location.split("/")[3], "UTF-8");
	Product p = data.getProductes().get(prod);

	User u = (User) request.getSession().getAttribute("user");
	if (u == null || p == null) {
	    response.sendRedirect("/Botiga");
	    return;
	}

	if (!u.getProducts().containsKey(p.getName())) {
	    showPage(request, response, "error403.jsp");
	    return;
	}

	ServletContext context = getServletContext();
	String realpath = context.getRealPath("/WEB-INF/products" + p.getPath());

	File file = new File(realpath);
	int length;
	ServletOutputStream outStream = response.getOutputStream();
	String mimetype = context.getMimeType(realpath);

	if (mimetype == null) {
	    mimetype = "application/octet-stream";
	}

	response.setContentType(mimetype);
	response.setContentLength((int) file.length());
	response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

	byte[] byteBuffer = new byte[1024];
	DataInputStream in = new DataInputStream(new FileInputStream(file));

	while ((length = in.read(byteBuffer)) != -1) {
	    outStream.write(byteBuffer, 0, length);
	}

	in.close();
	outStream.close();

    }

    private void controlWebServices(HttpServletRequest request, HttpServletResponse response) {
	
	/*
	Camps del JSON:
	    - status: LIMIT_INFERIOR_SALDO,
			LIMIT_SUPERIOR_SALDO,
			INVALID_STRING_SALDO,
			INVALID_USER_PASS
	    - saldoactual: saldoanterior + saldo nou
	*/
	
	String user = request.getParameter("user");
	String pass = request.getParameter("pass");
	
	JSONArray status = new JSONArray();
	boolean correct = true;
	try{
	    int augment = Integer.parseInt(request.getParameter("saldo"));
	    if(augment<5){
		status.put("LIMIT_INFERIOR_SALDO");
		correct = false;
	    }
	    if(augment > 3000) {
		status.put("LIMIT_SUPERIOR_SALDO");
		correct = false;
	    }
	}catch(NumberFormatException ex) {
	    status.put("INVALID_STRING_SALDO");
	    correct = false;
	}
    }
}
