/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import java.util.HashMap;

/**
 *
 * @author Xavi Moreno
 */
public class Bundle {

    private HashMap<String, Object> hashmap; //This HashMap connect a view with another view
    
    public Bundle() {
        hashmap = new HashMap<>();
    }
    
    public void putInt(String key, int i) {
        hashmap.put(key, i);
    }
    
    public void putFloat(String key, float f) {
        hashmap.put(key, f);
    }
    
    public void putString(String key, String s) {
        hashmap.put(key, s);
    }
    
    public void putObject(String key, Object obj) {
        hashmap.put(key, obj);
    }
    
    public int getInt(String key) {
        return (int) hashmap.get(key);
    }
    
    public float getFloat(String key) {
        return (float) hashmap.get(key);
    }
    
    public String getString(String key) {
        return (String) hashmap.get(key);
    }
    
    public Object getObject(String key) {
        return hashmap.get(key);
    }
}
