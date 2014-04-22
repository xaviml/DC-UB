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
import ub.common.UserInUseException;
import ub.exceptions.WrongAdresseeException;

/**
 * This class provides the view the necessary functions to interact with the
 * model.
 * 
 * @author Pablo Martinez
 */
public class ChatController {
    private final ChatModel chatModel;
    
    /**
     * Constructor of chatController.
     * @param listener 
     */
    public ChatController(ChatRoomListener listener) {
        this.chatModel = new ChatModel(listener);
    }
    
    /**
     * This function force the program to attempt a new connection with the server.
     * Requires the IP of the server, the port and the username. All errors are
     * notified with Exceptions.
     * 
     * @param IP
     * @param port
     * @param username
     * @throws RemoteException - Server not connected
     * @throws NotBoundException
     * @throws MalformedURLException - Bad URL
     * @throws UserInUseException - User is already in use.
     */
    public void register(String IP, int port, String username) throws RemoteException, NotBoundException, MalformedURLException, UserInUseException {
        chatModel.register(IP, port, username);
    }
    
    /**
     * Disconnect from the server.
     */
    public void disconnect(){
        chatModel.disconnect();
    }
    
    /**
     * Write a new message. Must provide the system a username and the message in
     * string form.
     * @param username
     * @param message
     * @throws WrongAdresseeException 
     */
    public void writeMessage(String username, String message) throws WrongAdresseeException{
        chatModel.writeMessage(username, message);
    }
    
    /**
     * Write message to a group. Instead of the username you must use a group
     * reference (gref).
     * @param gref
     * @param message 
     */
    public void writeMessageGroup(String gref, String message){
        chatModel.writeMessageGroup(gref, message);
    }
    
    /**
     * This is a feature used for the unicast conversations. When user types you
     * might use this function to notify the other client.
     * @param username 
     */
    public void userIsTyping(String username) {
        chatModel.userIsTypingSender(username);
    }
    
    /**
     * Just get your own username. Might be useful.
     * 
     * @return myUsername 
     */
    public String getUsername(){
        return chatModel.getMyUserName();
    }
    
    /**
     * With this function you can create a group. you must provide the list of 
     * clients that you want in the group and the groupName.
     * 
     * @param members
     * @param groupName 
     */
    public void addGroup(ArrayList<String> members, String groupName){
        chatModel.addGroup(members, groupName, null);
    }
    
    /**
     * Add another member to an existing group.
     * @param gref
     * @param username 
     */
    public void addGroupMember(String gref, ArrayList<String> username){
        chatModel.addGroupMemberAndNotify(gref, username);
    }
    
    /**
     * Leave an existing group.
     * @param gref 
     */
    public void leaveGroup(String gref){
        chatModel.leaveGroup(gref);
    }
}
