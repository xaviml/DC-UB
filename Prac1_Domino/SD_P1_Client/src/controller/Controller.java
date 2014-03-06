/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import controller.connection.GameController;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import ub.swd.model.connection.AbstractProtocol;

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
        try {
            if(mGameController == null) {

                    this.mGameController = new GameController(new Socket(ip,port), AbstractProtocol.ProtocolSide.CLIENT_SIDE);

            }else{
                this.mGameController = new GameController(this.mGameController.getSocket(), AbstractProtocol.ProtocolSide.CLIENT_SIDE);
            }
        } catch (IOException ex) {
                return null;
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
