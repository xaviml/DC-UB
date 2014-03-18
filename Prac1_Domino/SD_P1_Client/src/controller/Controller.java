/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import client.Constants;
import controller.connection.GameController;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import model.Stats;
import ub.swd.model.connection.AbstractProtocol;

/**
 * Controller of Domino client
 * 
 * @author Xavi Moreno
 */
public class Controller {
    String username;

    private String ip;
    private int port;

    private GameController mGameController;
    private Stats stats;

    public Controller(String ip, int port) {
        this.username = System.getProperty("user.name");
        this.ip = ip;
        this.port = port;
        this.stats = new Stats();
    }

    /**
     * This function create a connection via socket with a server.
     * 
     * @return 
     */

    public GameController createGame() {
        try {
            Socket s = new Socket();
            InetSocketAddress isa = new InetSocketAddress(ip, port);
            s.connect(isa, Constants.TIMEOUT);
            this.mGameController = new GameController(s, AbstractProtocol.ProtocolSide.CLIENT_SIDE);
        } catch (IOException ex) {
                return null;
        }
        return this.mGameController;
    }
    
    /**
     * Gets of username
     * 
     * @return 
     */

    public String getUserName() {
        return this.username;
    }

    /**
     * Sets of username
     * 
     * @param username 
     */
    
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets of IP
     * 
     * @return 
     */
    
    public String getIp() {
        return ip;
    }
    
    /**
     * Sets the IP of connection
     * 
     * @param ip 
     */

    public void setIp(String ip) {
        this.ip = ip;
    }
    
    /**
     * Gets the port of connection
     * 
     * @return 
     */

    public int getPort() {
        return port;
    }

    /**
     * Sets the port of connection
     * 
     * @param port 
     */
    
    public void setPort(int port) {
        this.port = port;
    }
    
    /**
     * Gets the stats of all games.
     * 
     * @return 
     */

    public Stats getStats() {
        return stats;
    }
}
