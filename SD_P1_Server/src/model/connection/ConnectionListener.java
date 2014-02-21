/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Here we include the server socket, that will serve all request, and will create
 * a connection for each one.
 * @author Pablo
 */
public class ConnectionListener extends Thread{
    private boolean listening;
    private ConnectionManager manager;
    private ServerSocket serverSocket;
    
    public ConnectionListener(ConnectionManager aThis){
        this.listening = false;
        this.manager = aThis;
    }
    
    @Override
    public void run(){
        while(listening){
            // Accept connections
        }
    }
    
    
    public void startListening() throws IOException{
        this.listening = true;
        this.serverSocket = new ServerSocket(Constants.PORT);
        this.start();
    }
    
    public void endListening() throws IOException{
        this.listening = false;
        this.serverSocket.close();
    }
}
