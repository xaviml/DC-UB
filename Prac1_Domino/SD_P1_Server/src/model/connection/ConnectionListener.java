/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import view.Log;

/**
 * Here we include the server socket, that will serve all request, and will create
 * a connection for each one.
 * @author Pablo
 */
public class ConnectionListener extends Thread{
    private boolean listening;
    private OnConnectListener listener;
    private ServerSocket serverSocket;
    private Log log;
    
    public ConnectionListener(Log log){
        this.listening = false;
        this.log = log;
    }
    
    public void setListener(OnConnectListener li){
        this.listener = li;
    }
    
    @Override
    public void run(){
        while(listening){
            try {
                // Accept connections
                Socket s = serverSocket.accept();
                listener.onConnect(s);
            } catch (IOException ex) {
                // Socket was closed, destroy the listener.
                log.write(this.getClass().getSimpleName(), "Server socket closed", Log.MessageType.MONITORING);
                this.listening = false;
            }
            
        }
    }
    
    
    public void startListening() throws IOException{
        this.listening = true;
        this.serverSocket = new ServerSocket(Constants.PORT);
        this.start();
    }
    
    public void endListening() throws IOException{
        this.serverSocket.close();
    }
    
    protected interface OnConnectListener{
        public void onConnect(Socket s);
    }
}
