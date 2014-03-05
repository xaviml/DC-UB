/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Xavi Moreno
 */
public class Controller {
    String username;
    
    private String ip;
    private int port;
    
    private GameController mGameController;
    
    public Controller(String ip, int port) {
        this.username = "Player";
        this.ip = ip;
        this.port = port;
    }
    
    
    public GameController createGame() {
        if(mGameController == null) {
            try {
                this.mGameController = new GameController(ip, port);
            } catch (IOException ex) {
                return null;
            }
        }else{
            this.mGameController = new GameController(this.mGameController.getSocket());
        }
        return this.mGameController;
    }
    
    public String getUserName() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
}
