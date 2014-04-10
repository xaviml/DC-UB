/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import ub.common.IPeer;
import ub.common.Message;

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
    public void userConnect(String username, IPeer peer) {
        chatModel.userConnected(username, peer);
    }

    @Override
    public void userDisconnect(String username) {
        chatModel.userDisconnected(username);
    }

    @Override
    public void userIsTyping(String username) {
        chatModel.userIsTypingReceiver(username);
    }

    @Override
    public void writeMessage(Message message) throws RemoteException {
        chatModel.recieveMessage(message);
    }

    @Override
    public void writeMessage(String ref, Message message) {
        chatModel.recieveMessageGroup(ref, message);
    }

    @Override
    public void ping() throws RemoteException {}

    @Override
    public void addGroup(String gref, String groupName, ArrayList<String> members) throws RemoteException {
        chatModel.addGroup(members, groupName, gref);
    }

    @Override
    public void notifyServerIsDown() throws RemoteException {
        chatModel.recieveServerDownFlag();
    }

    @Override
    public void userLeftGroup(String gref, String username) {
        chatModel.removeGroupMember(gref, username);
    }

    @Override
    public void userJoinedGroup(String gref, String username) {
        chatModel.addGroupMember(gref, username);
    }

    @Override
    public void leaveGroup(String gref, String username) throws RemoteException {
        chatModel.removeGroupMember(gref, username);
    }



}
