/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.controller;

import java.util.ArrayList;
import ub.common.IPeer;

/**
 *
 * @author Xavi Moreno
 */
public class Chat {
    public String name; //This is de chat name
    public ArrayList<IPeer> peers;
    public ArrayList<String> chat; //set of conversations
    
    
    public Chat(String name, ArrayList<IPeer> peers) {
        this.name = name;
        this.peers = peers;
    }
    
    public void addMember(IPeer peer) {
        peers.add(peer);
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
    
    public boolean isGroup() {
        return peers.size() > 1;
    }
}
