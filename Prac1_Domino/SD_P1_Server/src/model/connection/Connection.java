/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import java.net.Socket;
import view.Log;

/**
 * This class will be the Socket Listener, an active thread will be always listening
 * to the client, and interacting with him. This class also implements some methods
 * to allow it to interact with the gameController, if necessary.
 * 
 * @author Pablo
 */
public class Connection extends Thread{
    public static enum ConnectionState{CONNECTED,PLAYING,FINISHED,FORCEQUIT}
    private ConnectionState state;
    private Socket socket;
    private int ID;
    private Log log;   
     
    public Connection(Socket socket, int id, Log log){
        this.state = ConnectionState.CONNECTED;
        this.socket = socket;
        this.log = log;
        this.ID = id;
        
    }
    @Override
    public void run(){
        // Do something
    }

    protected String getIP() {
        return "null";
        //return socket.getInetAddress().getHostAddress();
    }

    protected void closeConnection() {
        this.state = ConnectionState.FORCEQUIT;
    }
    
    @Override
    public String toString(){
        return this.ID+":"+this.getIP();
        
    }
}
