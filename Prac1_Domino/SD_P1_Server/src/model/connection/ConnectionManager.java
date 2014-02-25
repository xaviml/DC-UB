/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.Log;

/**
 *
 * @author Pablo
 */
public class ConnectionManager {
    private ConcurrentHashMap<Integer,Connection> connections;
    private ConnectionListener listener;
    private int nextUid = 0;
    private Log log;
    
    public ConnectionManager(Log log){
        this.connections = new ConcurrentHashMap<>();
        this.log = log;
        this.log.write("[CM] :: Connection manager created");      
        
        this.nextUid = 0;
        this.log = log;
        
        // TEST
        for (int i = 0; i < 100; i++) {
            this.addConnection(new Socket());
        }
        
        for (int i = 0; i < 50; i++) {
            log.removeConnection(i+":null");
            
        }
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
    protected void addConnection(Socket s){
        nextUid+=1;
        int id = this.nextUid;
        Connection c = new Connection(s, id, log);
        this.connections.put(id, c);
        log.addConnection(c.toString());
    }
    
    /**
     * Close all connections!
     */
    protected void closeConnections() {
        for (Connection c : connections.values()) {
            c.closeConnection();
        }
    }

    public ArrayList<String> getConnections() {
        ArrayList<String> a = new ArrayList<>();
        for (Connection c: connections.values()){
            a.add(c.getId()+c.getIP());
        }
        return a;
    }
    
    
    
}
