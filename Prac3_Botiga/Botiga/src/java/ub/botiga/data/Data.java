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
	if(mUsers.containsKey(user)) return mUsers.get(user);
	User u = new User(user, 1000, new HashMap<String, Product>());
	mUsers.put(user, u);
	saveUsers();
	return u;
    }
    
    public void buyProduct(String user, String product) {
	
	//Aquí s'ha d'actualitzar el fitxer users.json també
	mUsers.get("pablo");
	saveUsers();
    }
    
    
}
