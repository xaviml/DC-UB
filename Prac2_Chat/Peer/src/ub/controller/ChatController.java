/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.controller;

import ub.model.ChatModel.ChatRoomListener;
import ub.model.ChatModel;
import ub.common.Message;
import ub.model.Peer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.IPeer;
import ub.common.IServer;

/**
 *
 * @author Xavi Moreno
 */
public class ChatController {
    private Peer myPeer;
    private IServer server;
    private final ChatModel chatModel;
    
    
    public ChatController(ChatRoomListener listener) {
        this.chatModel = new ChatModel(listener, myPeer);

    }
    
    public void register(String IP, String username) throws RemoteException, NotBoundException, MalformedURLException {
        myPeer = new Peer(username,chatModel);
        server = (IServer) Naming.lookup("rmi://localhost:1099/Server");
        ConcurrentHashMap<String,IPeer> con = server.registryUser(myPeer.getUsername(), myPeer);
        chatModel.setConnections(con);
        //dateServer.registryUser(myPeer);
    }
    
    public void disconnect(){
        
    }
    
    public ArrayList<String> getConnectedPeers(){
        return chatModel.getConnectedClients();
    }
    
    public boolean writeMessage(String username, String message) throws RemoteException{
        // TODO: Control if client isnt connected anymore
        return chatModel.writeMessage(username, message);
    }
    
    
}
