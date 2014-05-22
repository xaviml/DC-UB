/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.botiga.data;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import ub.botiga.utils.Utils;

/**
 *
 * @author zenbook
 */
public class Data {
    
    private final HashMap<String, User> mUsers;
    private final HashMap<String, Product> mProducts;
    
    private final Object mutex = new Object();
    
    public Data(String users, String products) {
	//mProducts = Utils.loadProducts("/home/zenbook/SWD/Dijous2/Prac3_Botiga/Botiga/web/WEB-INF/products.json");
	//mUsers = Utils.loadUsers("/home/zenbook/SWD/Dijous2/Prac3_Botiga/Botiga/web/WEB-INF/users.json",mProducts);
	mProducts = Utils.loadProducts(products);
	mUsers = Utils.loadUsers(users, mProducts);
    }
    
    private void saveUsers() {
	try {
	    Utils.saveUsers("/home/zenbook/SWD/Dijous2/Prac3_Botiga/Botiga/web/WEB-INF/users.json", mUsers, mutex);
	} catch (JSONException ex) {
	    System.err.println("Error");
	} catch (FileNotFoundException ex) {
	    Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    
    /**
     * Add user if not exists
     * 
     * @param user 
     * @return  
     */
    
    public User addUser(String user) {
	User u;
	synchronized(mUsers) {
	    if(mUsers.containsKey(user)) return mUsers.get(user);
	    u = new User(user, 1000, new HashMap<String, Product>());
	    mUsers.put(user, u);	
	}
	saveUsers();
	return u;
    }
    
    public void buyProduct(User user, HashMap<String, Product> productes, float preu) {
	//Aquí s'actualitzar el fitxer users.json també
	synchronized(user) {
	    user.getProducts().putAll(productes);
	    user.setCredits(user.getCredits()-preu);
	}
	saveUsers();
    }

    public HashMap<String, Product> getProductes() {
	return mProducts;
    }

    public HashMap<String, User> getUsers() {
	return mUsers;
    }

    public void augmentarSaldo(User u, int augment) {
	synchronized(u) {
	    u.setCredits(u.getCredits()+augment);
	}
	saveUsers();
    }
    
    
}
