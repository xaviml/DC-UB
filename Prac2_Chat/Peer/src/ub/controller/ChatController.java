/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.controller;

import ub.model.Peer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import ub.common.IServer;
import ub.model.Chat.ChatListener;
import ub.model.ChatModel;
import ub.model.Message;

/**
 *
 * @author Xavi Moreno
 */
public class ChatController {
    private Peer myPeer;
    private IServer server;
    private final ChatModel chatModel;
    
    
    public ChatController() {
        this.chatModel = new ChatModel();

    }
    
    public void register(String IP, String username) throws RemoteException, NotBoundException, MalformedURLException {
        myPeer = new Peer(username,chatModel);
        server = (IServer) Naming.lookup("rmi://localhost:1099/Server");
        server.registryUser(myPeer);
        //dateServer.registryUser(myPeer);
    }
    
    public void disconnect(){
        
    }
    
    public String getConnectedPeers(){
        return server.getUsers();
    }
    
    public boolean newChat(String clientName, ChatListener listener){
        return false;
    }
   
    public ArrayList<Message> retrieveChatMessages(long chatid){
        return null;
    }
    
    
    
}
