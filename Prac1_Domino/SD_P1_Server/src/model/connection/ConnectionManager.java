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
            this.listener.setName("Connection-Listener");
            
            // Set on
            listener.startListening();
            this.log.write(this.getClass().getSimpleName(),"Listener started", Log.MessageType.MONITORING);   
            return true;
            
        } catch (IOException ex) {
            // Cannot start ServerSocket
            this.log.write(this.getClass().getSimpleName(),"Cannot start server socket. Is another istance of the program running?", Log.MessageType.ERROR); 
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

    public ArrayList<String> getConnections() {
        ArrayList<String> a = new ArrayList<>();
        for (Connection c: connections.values()){
            a.add(c.getId()+c.getIP());
        }
        return a;
    }

    @Override
    public void onDisconnect(int id) {
        log.removeConnection(connections.remove(id).toString());
    }

    @Override
    public void onConnect(Socket s) {
        this.log.write(this.getClass().getSimpleName(),"New connection from "+s.getInetAddress().getHostAddress()+".", Log.MessageType.CONNECTION); 
        nextUid+=1;
        int id = this.nextUid;
        
        Connection c = new Connection(s, id, log);
        
        if (connections.size() >= Constants.MAX_CONNECTIONS){
            // TODO: Reject connection
        }
        c.setListener(this);
        
        this.connections.put(id, c);
        log.addConnection(c.toString());
        c.setName("Connection-"+id);
        c.start();
    }
    
    
    
    
    
}
