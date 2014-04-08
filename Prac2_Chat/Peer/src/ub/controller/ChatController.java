/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.controller;

import ub.model.ChatModel.ChatRoomListener;
import ub.model.ChatModel;
import ub.model.Peer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import ub.common.IPeer;
import ub.common.IServer;
import ub.common.InvalidUserNameException;

/**
 *
 * @author Xavi Moreno
 */
public class ChatController {
    private Peer myPeer;
    private IServer server;
    private ChatModel chatModel;
    
    
    public ChatController(ChatRoomListener listener) {
        this.chatModel = new ChatModel(listener);
    }
    
    public void register(String IP, int port, String username) throws RemoteException, NotBoundException, MalformedURLException, InvalidUserNameException {
        chatModel.register(IP, port, username);
    }
    
    public void disconnect(){
        
    }
    
    public ArrayList<String> getConnectedPeers(){
        return chatModel.getConnectedClients();
    }
    
    public boolean writeMessage(String username, String message) {
        // TODO: Control if client isnt connected anymore
        return chatModel.writeMessage(username, message);
    }
    
    public String getUsername() {
        return myPeer.getUsername();
    }
    
    
}
