/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.IPeer;
import ub.common.IServer;

/**
 *
 * @author Xavi Moreno
 */
public class Peer extends UnicastRemoteObject implements IPeer{
    private ChatModel chatModel;
    private String username;
    
    public Peer(String name, ChatModel model) throws RemoteException {
        this.username = name;
        this.chatModel = model;
    }
    
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void userConnect(String username, IPeer peer) {
        chatModel.userConnected(username, peer);
    }

    @Override
    public void userDisconnect(IPeer peer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public void userIsTyping() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeMessage(float idChat, IPeer peer, String message) {
        
    }

    @Override
    public void addGroup(float idChat, String group, IPeer[] peers) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }



}
