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
        this.username = "Player";
        this.ip = ip;
        this.port = port;
        this.stats = new Stats();
    }


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

    public String getUserName() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Stats getStats() {
        return stats;
    }
}
