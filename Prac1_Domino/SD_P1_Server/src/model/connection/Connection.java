/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import java.io.IOException;
import java.net.Socket;
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
    private int ID;
    private Log log;   
     
    public Connection(Socket socket, int id, Log log){
        this.state = ConnectionState.CONNECTED;
        this.socket = socket;
        this.log = log;
        this.ID = id;
        
        try {
            this.com = new ComUtils(socket);
        } catch (IOException ex) {
            this.log.write(this.getClass().getSimpleName(),"Cannot establish communication with "+this.ID, Log.MessageType.ERROR); 
        }
    }
    @Override
    public void run(){
        while(state != ConnectionState.FORCEQUIT /*|| state != ConnectionState.FINISHED*/){
            try {
                com.write_int32(ID);
                try {
                    this.sleep(1000);
                } catch (InterruptedException ex1) {
                }
            } catch (IOException ex) {
                this.log.write(this.getClass().getSimpleName(),"Connection "+this.ID+" caused an IOexception. Disconnecting...", Log.MessageType.ERROR);
                this.state = ConnectionState.FORCEQUIT;

            }
        }
        try {
            //Finish connection
            socket.close();
        } catch (IOException ex) {
            //
        }
        dcListener.onDisconnect(ID);
    }

    protected void setListener(OnDisconnectListener l){
        this.dcListener = l;
    }
    
    protected String getIP() {
        return socket.getInetAddress().getHostAddress();
    }

    protected void closeConnection() {
        this.state = ConnectionState.FORCEQUIT;
        
    }
    
    @Override
    public String toString(){
        return this.ID+" : "+this.getIP();
        
    }
    
    protected interface OnDisconnectListener{
        public void onDisconnect(int id);
    }
}
