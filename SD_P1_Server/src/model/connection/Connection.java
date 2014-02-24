/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.connection;

import java.net.Socket;

/**
 * This class will be the Socket Listener, an active thread will be always listening
 * to the client, and interacting with him. This class also implements some methods
 * to allow it to interact with the gameController, if necessary.
 * 
 * @author Pablo
 */
public class Connection extends Thread{
    private Socket socket;
    
    
    @Override
    public void run(){
        // Do something
    }
}
