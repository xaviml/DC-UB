/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import java.util.ArrayList;
import model.connection.ConnectionManager;
import view.Log;

/**
 *
 * @author Pablo
 */
public class ServerController {
    private ConnectionManager conManager;
    private Log log;
    
    public ServerController(Log log){
        this.log = log;
        log.write("SC :: Server controller created");
        this.conManager = new ConnectionManager(log);
        
    }
    
    
    /// CONNECTION MANAGEMENT ///
    
    public void acceptNewConnections(){
        conManager.startListening();
    }
    
    public void rejectNewConnections(){
        conManager.stopListening();
    }
    
    public void closeConnections(){
        //conManager.closeConnections();
    }
    
    public ArrayList<String> retrieveConnections(){
        return conManager.getConnections();
    }
    
    
    /// LOGGER MANAGEMENT ///
    public void toggleGameInfo(){
        
    }
    
    public void toggleNewConnectionsInfo(){
        
    }
    
    public void toggleConnectionExceptionsInfo(){
        
    }
    
    public void toggleBandWidthInfo(){
        
    }
}
