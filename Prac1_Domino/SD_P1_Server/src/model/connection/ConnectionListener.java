/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import model.Constants;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import view.Log;

/**
 * Here we include the server socket, that will serve all request, and will create
 * a connection for each one.
 * @author Pablo
 */
public class ConnectionListener implements Runnable{
    private boolean listening;
    private OnConnectListener listener;
    private ServerSocket serverSocket;
    private Log log;
    
    /**
     * Constructor, receives a log object.
     * @param log 
     */
    public ConnectionListener(Log log){
        this.listening = false;
        this.log = log;
    }
    
    /**
     * Set a connection listener.
     * @param li 
     */
    public void setListener(OnConnectListener li){
        this.listener = li;
    }
    
    /**
     * Overwritten run method, performs as a thread.
     */
    @Override
    public void run(){
        while(listening){
            try {
                // Accept connections
                Socket s = serverSocket.accept();
                s.setSoTimeout(Constants.TIMEOUT_MILLIS);
                listener.onConnect(s);
            } catch (IOException ex) {
                // Socket was closed, destroy the listener.
                log.write(this.getClass().getSimpleName(), "Server socket closed", Log.MessageType.MONITORING);
                this.listening = false;
            }
            
        }
    }
    
    /**
     * Start listening incoming connections.
     * @throws IOException When we aren't able to create a ServerSocket.
     */
    public void startListening() throws IOException{
        this.listening = true;
        this.serverSocket = new ServerSocket(Constants.PORT);
    }
    
    /**
     * Stop listening incoming connections
     * @throws IOException When serverSocket is stuck in a blocking action.
     */
    public void endListening() throws IOException{
        this.serverSocket.close();
    }
    
    /**
     * This interface notify when a new connection is accepted.
     */
    protected interface OnConnectListener{
        public void onConnect(Socket s);
    }
}
