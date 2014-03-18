/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import model.connection.ConnectionManager;
import view.Log;

/**
 * This is the controller of the server. Here we control the actions performed by
 * the user, such as setting on/off the connection listener.
 * 
 * @author Pablo
 */
public class ServerController {
    private final ConnectionManager conManager;
    private final Log log;
    
    /**
     * This constructors recieves a Log as parameter, in order to be able to
     * write on it.
     * @param log 
     */
    public ServerController(Log log){
        this.log = log;
        
        log.write(this.getClass().getSimpleName(),"Server controller created", Log.MessageType.MONITORING);
        this.conManager = new ConnectionManager(log);
        
    }
    
    
    /// CONNECTION MANAGEMENT ///
    
    /**
     * Start listening connections.
     * @return action has been performed succesfully.
     */
    public boolean acceptNewConnections(){
        return conManager.startListening();
    }
    
    /**
     * Stop listening connections.
     * @return action has been performed succesfully.
     */
    public boolean rejectNewConnections(){
        return conManager.stopListening();
    }
    
    /**
     * Close single connection
     * @param i Connection id 
     */
    public void closeConnection(int i){
        conManager.closeConnection(i);
    }
    
    /**
     * Close all connections.
     */
    public void closeConnections(){
        conManager.closeConnections();
    }
    
    /// LOGGER MANAGEMENT ///
    /**
     * Toggle on/off the Game logs.
     */
    public void toggleGames(){
        log.toggleGames();
    }
    /**
     * Toggle on/off the Connection logs.
     */
    public void toggleConnections(){
        log.toggleConnections();
    }
    /**
     * Toggle on/off the Error logs.
     */
    public void toggleErrors(){
        log.toggleErrors();
    }
    
}
