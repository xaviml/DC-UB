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
        server.registryUser(myPeer);
        //dateServer.registryUser(myPeer);
    }
    
    public void disconnect(){
        
    }
    
    public ArrayList<String> getConnectedPeers(){
        return chatModel.getConnectedClients();
    }
    
   
    public ArrayList<Message> retrieveChatMessages(long chatid){
        return null;
    }
    
    
    
}
