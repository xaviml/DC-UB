/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.botiga.data;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author zenbook
 */
public class User {
    private String mName; //Name must be unique.
    private float mCredit;
    private HashMap<String, Product> mProducts;

    public User(String mName, float mCredits, HashMap<String, Product> mProducts) {
	this.mName = mName;
	this.mCredit = mCredits;
	this.mProducts = mProducts;
    }
    
    public User(JSONObject obj, HashMap<String, Product> totalproducts) throws JSONException {
	this.mName = obj.getString("name");
	this.mCredit = (float) obj.getDouble("credit");
	this.mProducts = new HashMap<String, Product>();
	JSONArray m = obj.getJSONArray("products");
	String p;
	for (int i = 0; i < m.length(); i++) {
	    p = m.getString(i);
	    if(totalproducts.containsKey(p)) {
		mProducts.put(p, totalproducts.get(p));
	    }
	}
	
		
    }

    public String getName() {
	return mName;
    }

    public void setName(String mName) {
	this.mName = mName;
    }

    public float getCredits() {
	return mCredit;
    }

    public void setCredits(float mCredits) {
	this.mCredit = mCredits;
    }

    public HashMap<String, Product> getProducts() {
	return mProducts;
    }

    public void setProducts(HashMap<String, Product> mProducts) {
	this.mProducts = mProducts;
    }
    
    public void save(JSONObject root) throws JSONException {
	root.put("name", mName);
	root.put("credit",mCredit);
	
	JSONArray array = new JSONArray();
	for (Product p : mProducts.values()) {
	    array.put(p.getName());
	}
	root.put("products", array);
    }
}
