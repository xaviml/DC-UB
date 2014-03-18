/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.framework;

import java.util.HashMap;

/**
 * This class connects a view with other view.
 * 
 * @author Xavi Moreno
 */
public class Bundle {

    /**
     * This hashmap connects a view with other view.
     * 
     */
    private HashMap<String, Object> hashmap;
    
    public Bundle() {
        hashmap = new HashMap<>();
    }
    
    /**
     * Puts a int on hashmap.
     * 
     * @param key
     * @param i 
     */
    
    public void putInt(String key, int i) {
        hashmap.put(key, i);
    }
    /**
     * Puts a float on hasmap.
     * 
     * @param key
     * @param f 
     */
    
    public void putFloat(String key, float f) {
        hashmap.put(key, f);
    }
    
    /**
     * Puts a String on hashmap.
     * 
     * @param key
     * @param s 
     */
    
    public void putString(String key, String s) {
        hashmap.put(key, s);
    }
    
    /**
     * Puts a Object on hashmap.
     * 
     * @param key
     * @param obj 
     */
    
    public void putObject(String key, Object obj) {
        hashmap.put(key, obj);
    }
    
    /**
     * Gets a integer.
     * 
     * @param key
     * @return 
     */
    
    public int getInt(String key) {
        return (int) hashmap.get(key);
    }
    
    /**
     * Gets a float.
     * 
     * @param key
     * @return 
     */
    
    public float getFloat(String key) {
        return (float) hashmap.get(key);
    }
    
    /**
     * Gets a String.
     * 
     * @param key
     * @return 
     */
    
    public String getString(String key) {
        return (String) hashmap.get(key);
    }
    
    /**
     * Gets a Object.
     * 
     * @param key
     * @return 
     */
    
    public Object getObject(String key) {
        return hashmap.get(key);
    }
}
