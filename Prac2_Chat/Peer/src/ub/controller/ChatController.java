/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.controller;

import ub.model.ChatModel.ChatRoomListener;
import ub.model.ChatModel;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import ub.common.GroupReference;
import ub.common.UserInUseException;
import ub.exceptions.WrongAdresseeException;

/**
 *
 * @author Xavi Moreno
 */
public class ChatController {
    private final ChatModel chatModel;
    
    
    public ChatController(ChatRoomListener listener) {
        this.chatModel = new ChatModel(listener);
    }
    
    public void register(String IP, int port, String username) throws RemoteException, NotBoundException, MalformedURLException, UserInUseException {
        chatModel.register(IP, port, username);
    }
    
    public void disconnect(){
        chatModel.disconnect();
    }
    
    public void writeMessage(String username, String message) throws WrongAdresseeException{
        chatModel.writeMessage(username, message);
    }
    
    public void writeMessage(GroupReference gref, String message){
        chatModel.writeMessage(gref, message);
    }
    
    public void userIsTyping(String username) {
        chatModel.userIsTypingSender(username);
    }
    
    public String getUsername(){
        return chatModel.getMyUserName();
    }
    public void addGroup(ArrayList<String> members, String groupName){
        chatModel.addGroup(members, groupName, null);
    }
    public void addGroupMember(GroupReference gref, String username){
        
    }
    public void leaveGroup(GroupReference gref){
        
    }
}
