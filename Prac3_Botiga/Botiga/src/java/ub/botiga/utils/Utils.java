/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.botiga.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import ub.botiga.data.Product;
import ub.botiga.data.User;

import org.json.JSONObject;

/**
 *
 * @author zenbook
 */
public class Utils {

    public static HashMap<String, Product> loadProducts(String file) {
	String text = getStringFile(file);
	HashMap<String, Product> out = new HashMap<String, Product>();
	if(text == null)
	    return out;
	try {
	    JSONObject obj = new JSONObject(text);
	    JSONArray array = obj.getJSONArray("products");
	    Product p;
	    for (int i = 0; i < array.length(); i++) {
		p = new Product(array.getJSONObject(i));
		out.put(p.getName(), p);
	    }
	    return out;
	} catch (JSONException ex) {
	    System.err.println("Problemes al parsejar el json");
	}
	
	return null;
    }

    public static HashMap<String, User> loadUsers(String file, HashMap<String, Product> mProducts) {
	String text = getStringFile(file);
	HashMap<String, User> out = new HashMap<String, User>();
	if(text == null)
	    return out;
	try {
	    JSONObject obj = new JSONObject(text);
	    JSONArray array = obj.getJSONArray("users");
	    User u;
	    for (int i = 0; i < array.length(); i++) {
		u = new User(array.getJSONObject(i), mProducts);
		out.put(u.getName(), u);
	    }
	    return out;
	} catch (JSONException ex) {
	    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	}
	return null;
    }
    
    private static String getStringFile(String file) {
	BufferedReader reader = null;
	try {
	    reader = new BufferedReader( new FileReader (file));
	    String line;
	    StringBuilder stringBuilder = new StringBuilder();
	    String ls = System.getProperty("line.separator");
	    while( ( line = reader.readLine() ) != null ) {
		stringBuilder.append( line );
		stringBuilder.append( ls );
	    }
	    return stringBuilder.toString();
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IOException ex) {
	    Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    try {
		if(reader != null)
		    reader.close();
	    } catch (IOException ex) {
		Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}
	return null;
    }
    
    
    
    public static Product.FileType getFileType(String type) {
	if(type.equals("mp3")) return Product.FileType.MP3;
	else if(type.equals("pdf")) return Product.FileType.PDF;
	else if(type.equals("video")) return Product.FileType.VIDEO;
	else return Product.FileType.UNDEFINED;
    }

    public static void saveUsers(String file, HashMap<String, User> mUsers, Object mutex) throws JSONException, FileNotFoundException {
	    JSONObject root = new JSONObject();
	    JSONArray array = new JSONArray();
	    JSONObject obj;
	    for (User u : mUsers.values()) {
		obj = new JSONObject();
		u.save(obj);
		array.put(obj);
	    }
	    root.put("users", array);
	    
	    synchronized(mutex) {
		PrintWriter out = new PrintWriter(file);
		out.write(root.toString(2));
		out.close();
	    }
    }
}
