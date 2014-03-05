/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavi Moreno
 */
public class GameController {

    private Socket mSocket;
    private DominoGame mGame;
    
    public GameController(Socket s) {
        this.mSocket = s;
    }
    
    public GameController(String ip, int port) throws IOException {
        this(new Socket(ip, port));
    }
    
    
    
    
    
    public void closeGame() {
        try {
            mSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(GameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Socket getSocket() {return mSocket; }
    

}
