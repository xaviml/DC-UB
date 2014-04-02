/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ub.common.IPeer;

/**
 *
 * @author Xavi Moreno
 */
public class Chat {
    public String name; //This is de chat name
    public ArrayList<IPeer> peers;
    public ArrayList<String> chat; //set of conversations
    public boolean groupFlag = false;
    
    
    public Chat(String name) {
        this.name = name;
    }
    
    
    
    public boolean addUser(IPeer peer) {
        // Check if the peer is already in the list
        if (peers.contains(peer)) return false;
        // Add it to the list
        peers.add(peer);
        // Check if this chat has become a group
        if (!groupFlag && peers.size()>1){
            groupFlag = true;
        }
        return true;
    }
    
    /**
     * This function remove a user and returns true if 
     * there are users in this chat, otherwise returns false;
     * 
     * @param peer
     * @return boolean
     */
    
    public boolean removeMember(IPeer peer) {
        peers.remove(peer);
        return !peers.isEmpty();
    }
    
    public void recieveMessage(){
        
    }
    
    public void writeMessage(IPeer peer, String message) throws RemoteException{
        synchronized(chat){
            chat.add(message);
        }
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (IPeer p : peers) {
            if (p.equals(this)) continue; // Don't send it to yourself
            executor.execute(new Writer(peer, p, message));
        }
    }
    
    
    public boolean isGroup() {
        return groupFlag;
    }
}
