/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.botiga.data;

import org.json.JSONException;
import org.json.JSONObject;
import ub.botiga.utils.Utils;

/**
 *
 * @author zenbook
 */
public class Product {

    public enum FileType {MP3, PDF, VIDEO, UNDEFINED};
    
    private FileType mType;
    private String mName; //The name must be unique
    private String mDescription;
    private float mPrice;
    private String path;

    public Product(FileType mType, String mName, String mDescription, int mPrice, String path) {
	this.mType = mType;
	this.mName = mName;
	this.mDescription = mDescription;
	this.mPrice = mPrice;
	this.path = path;
    }
    
    public Product(JSONObject obj) throws JSONException {
	this.mName = obj.getString("name");
	this.mType = Utils.getFileType(obj.getString("type"));
	this.mDescription = obj.getString("desc");
	this.mPrice = (float) obj.getDouble("price");
	this.path = obj.getString("path");
    }

    public FileType getType() {
	return mType;
    }

    public void setType(FileType mType) {
	this.mType = mType;
    }

    public String getName() {
	return mName;
    }

    public void setName(String mName) {
	this.mName = mName;
    }

    public String getDescription() {
	return mDescription;
    }

    public void setDescription(String mDescription) {
	this.mDescription = mDescription;
    }

    public float getPrice() {
	return mPrice;
    }

    public void setPrice(float mPrice) {
	this.mPrice = mPrice;
    }

    public String getPath() {
	return path;
    }

    public void setPath(String path) {
	this.path = path;
    }
    
}
