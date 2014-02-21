/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import view.Log;

/**
 *
 * @author Pablo
 */
public class ConnectionManager {
    private ConcurrentHashMap<Integer,Connection> connections;
    private ConnectionListener listener;
    private int nextUid;
    private Log log;
    
    public ConnectionManager(Log log){
        this.nextUid = 0;
        this.log = log;
    }

    // LISTENER METHODS //
    public void startListening() {
        try {
            listener.startListening();
        } catch (IOException ex) {
            // Cannot start ServerSocket
            log.write("[CM] == Cannot start server socket.");
        }
    }

    public void stopListening() {
        try {
            listener.endListening();
        } catch (IOException ex) {
            log.write("[CM] == Cannot end listener properly.");
            // Cannot end listening
        }
    }

    // CONNECTION METHODS //
    public void closeConnections() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public ArrayList<String> getConnections() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
