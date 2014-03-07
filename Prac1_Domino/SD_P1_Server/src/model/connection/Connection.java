/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import ub.swd.model.connection.ComUtils;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private OnDisconnectListener dcListener;
    private ConnectionState state;
    private ComUtils com;
    private Socket socket;
    private Protocol protocol;
    private int ID;
    public Log log;   
     
    public Connection(Socket socket, int id, Log log){
        try {
            this.state = ConnectionState.CONNECTED;
            this.socket = socket;
            this.log = log;
            this.protocol = new Protocol(socket, log);
            this.ID = id;
        } catch (IOException ex) {
            System.err.println("Cannot create Protocol");
        }
        

    }
    @Override
    public void run(){
        while(state != ConnectionState.FORCEQUIT && state != ConnectionState.FINISHED){
            try {
                protocol.readFrame();
            } catch (SocketTimeoutException to){
                this.log.write(this.getClass().getSimpleName(),"Connection "+this.ID+" timed out. Disconnecting...", Log.MessageType.ERROR);
                this.state = ConnectionState.FORCEQUIT;
            }
            catch (IOException ex) {
                this.log.write(this.getClass().getSimpleName(),"Connection "+this.ID+" caused an IOexception. Disconnecting...", Log.MessageType.ERROR);
                this.state = ConnectionState.FORCEQUIT;
            } 
        }
        dcListener.onDisconnect(ID);
    }

    protected void setListener(OnDisconnectListener l){
        this.dcListener = l;
    }
    
    protected String getIP() {
        return socket.getInetAddress().getHostAddress();
    }
    
    protected Socket getSocket(){
        return socket;
    }

    protected void closeConnection() {
        this.state = ConnectionState.FORCEQUIT;
        try {
            this.socket.close();
        } catch (IOException ex) {
            this.log.write(this.getClass().getSimpleName(),"Connection "+this.ID+" caused an IOexception when was being closed.", Log.MessageType.ERROR);
        }
        
    }
    
    @Override
    public String toString(){
        return this.ID+" : "+this.getIP();
        
    }
    
    
    protected interface OnDisconnectListener{
        public void onDisconnect(int id);
    }
}
