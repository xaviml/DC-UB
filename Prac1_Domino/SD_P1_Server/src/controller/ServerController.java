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
        
        log.write(this.getClass().getSimpleName(),"Server controller created", Log.MessageType.MONITORING);
        this.conManager = new ConnectionManager(log);
        
    }
    
    
    /// CONNECTION MANAGEMENT ///
    
    public boolean acceptNewConnections(){
        return conManager.startListening();
    }
    
    public boolean rejectNewConnections(){
        return conManager.stopListening();
    }
    
    public void closeConnection(int i){
        conManager.closeConnection(i);
    }
    
    public void closeConnections(){
        conManager.closeConnections();
    }
    
    /// LOGGER MANAGEMENT ///
    public void toggleGames(){
        log.toggleGames();
    }
    
    public void toggleConnections(){
        log.toggleConnections();
    }
    
    public void toggleErrors(){
        log.toggleErrors();
    }
    
    
    /**
     * Not implemented
     */
    public void toggleBandWidthInfo(){
        
    }
}
