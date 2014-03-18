/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import model.Constants;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import view.Log;

/**
 * This class is on the top of the model. Includes all the connections, and
 * manages all the new connections, and all the disconnections.
 * @author Pablo
 */
public class ConnectionManager implements Connection.OnDisconnectListener, ConnectionListener.OnConnectListener{
    private final ConcurrentHashMap<Integer,Connection> connections;
    private ConnectionListener listener;
    private int nextUid = 0;
    private Log log;
    
    /**
     * Constructor, requires a Log.
     * @param log 
     */
    public ConnectionManager(Log log){
        this.connections = new ConcurrentHashMap<>();
        this.log = log;
        this.log.write(this.getClass().getSimpleName(),"Connection manager created", Log.MessageType.MONITORING);      
        
        this.nextUid = 0;
        this.log = log;
    }

    // LISTENER METHODS //
    
    /**
     * Start the new connection listener.
     * @return flag. Cant/cannot start the connectionListener.
     */
    public boolean startListening() {
        try {
            // Create a new listener
            this.listener = new ConnectionListener(log);
            this.listener.setListener(this);
            Thread conListener = new Thread(this.listener);
            conListener.setName("Connection-Listener");
            
            // Set it on
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

    
    /**
     * Stop the new connection listener.
     * @return flag. Cant/cannot stop the connectionListener.
     */
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
    
    /**
     * Close a single connection.
     * @param id ID of the connection
     */
    public void closeConnection(int id){
        Connection c = connections.get(id);
        c.closeConnection();
    }
    
    /**
     * Action performed when a client is disconnected.
     * @param id The id of the connection
     */
    @Override
    public void onDisconnect(int id) {
        Connection c = connections.remove(id);          // Remove it from HashMap.
        log.removeConnection(c.toString());             // Remove it from GUI.
    }
    

    /**
     * Action performed when a new client gets connected.
     * @param socket The socket of the client
     */
    @Override
    public void onConnect(Socket socket) {
        this.log.write(this.getClass().getSimpleName(),"New connection from "+socket.getInetAddress().getHostAddress()+".", Log.MessageType.CONNECTION); 
        
        // This not a good way to do this.. but it works
        nextUid = (nextUid+1%Constants.MAX_CONNECTIONS+100);
        while(connections.contains(nextUid)){
            nextUid = (nextUid+1%Constants.MAX_CONNECTIONS+100);
        }
        int id = this.nextUid;
        //
        
        Connection c;
        try {
            // The statement connections.size() >= Constants.MAX_CONNECTIONS check
            // if it's possible to serve the new connection.
            // a 'false' result will be treated in Protocol.
            c = new Connection(socket, id,(connections.size() < Constants.MAX_CONNECTIONS), log);
        } catch (IOException ex) {
            try {
                // Exception here means that IO data streams could not be created.
                // Just dont add the connection .. :/
                socket.close();
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
