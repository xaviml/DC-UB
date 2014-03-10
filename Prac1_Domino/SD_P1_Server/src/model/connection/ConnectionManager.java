/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import model.Constants;
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
public class ConnectionManager implements Connection.OnDisconnectListener, ConnectionListener.OnConnectListener{
    private ConcurrentHashMap<Integer,Connection> connections;
    private ConnectionListener listener;
    private int nextUid = 0;
    private Log log;
    
    public ConnectionManager(Log log){
        //this.listener = new ConnectionListener();
        this.connections = new ConcurrentHashMap<>();
        this.log = log;
        this.log.write(this.getClass().getSimpleName(),"Connection manager created", Log.MessageType.MONITORING);      
        
        this.nextUid = 0;
        this.log = log;
    }

    // LISTENER METHODS //
    public boolean startListening() {
        try {
            // Create a new listener
            this.listener = new ConnectionListener(log);
            this.listener.setListener(this);
            Thread conListener = new Thread(this.listener);
            conListener.setName("Connection-Listener");
            
            // Set on
            listener.startListening();
            conListener.start();
            this.log.write(this.getClass().getSimpleName(),"Listener started", Log.MessageType.MONITORING);   
            return true;
            
        } catch (IOException ex) {
            // Cannot start ServerSocket
            this.log.write(this.getClass().getSimpleName(),"Cannot start server socket. Is another istance of this server running?", Log.MessageType.ERROR); 
            return false;
        }
    }

    public boolean stopListening() {
        try {
            this.listener.endListening();
            this.listener = null; // Unlink the object to allow GarbageCollector remove it
            this.log.write(this.getClass().getSimpleName(),"Listener stopped", Log.MessageType.MONITORING);   
            return true;
        } catch (IOException ex) {
            // This might never happens...
            this.log.write(this.getClass().getSimpleName(),"Cannot end listener.", Log.MessageType.ERROR); 
            // Cannot end listening
            return false;
        }
    }

    // CONNECTION METHODS //
    
    /**
     * Close all connections!
     */
    public void closeConnections() {
        for (Connection c : connections.values()) {
            c.closeConnection();
        }
    }
    
    
    public void closeConnection(int i){
        Connection c = connections.get(i);
        c.closeConnection();
    }
    
    @Override
    public void onDisconnect(int id) {
        Connection c = connections.remove(id);          // Remove it from HashMap.
        log.removeConnection(c.toString());             // Remove it from GUI.
    }
    
    

    @Override
    public void onConnect(Socket s) {
        this.log.write(this.getClass().getSimpleName(),"New connection from "+s.getInetAddress().getHostAddress()+".", Log.MessageType.CONNECTION); 
        
        /* Reject incoming connections if the limit is reached */
        if (connections.size() >= Constants.MAX_CONNECTIONS){
            try {
                s.close();
                this.log.write(this.getClass().getSimpleName(), "Connection limit reached.", Log.MessageType.ERROR);
            } catch (IOException ex) {
                this.log.write(this.getClass().getSimpleName(), "Socket was already closed. Connection limit reached.", Log.MessageType.ERROR);
            }
            return;
        }
        // This not a good way to do this.. but it works
        // TODO: If there are not empty slots, scape the function.
        // This may jump into an infinite loop, in case that there are 99999 connections
        // at the same time.
        nextUid = (nextUid+1%99999);
        while(connections.contains(nextUid)){
            nextUid = (nextUid+1%99999);
        }
        int id = this.nextUid;
        //
        
        Connection c;
        try {
            c = new Connection(s, id, log);
        } catch (IOException ex) {
            try {
                // Exception here means that IO data streams could not be created.
                // Just dont add the connection .. :/
                s.close();
            } catch (IOException ex1) {
                // Maybe the socket was already closed...
                // If you arrive here just cry.
                System.err.println("T_T");
            }
            return;
        }
        

        c.setListener(this);
        
        this.connections.put(id, c);
        log.addConnection(c.toString());
        
        
        Thread t = new Thread(c);
        // TODO: Save an instance of all threads anywhere.
        t.setName("Connection-"+id);
        t.start();
    }
    
    
    
    
    
}
