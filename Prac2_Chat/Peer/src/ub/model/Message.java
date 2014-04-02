/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

/**
 *
 * @author Pablo
 */
public class Message {
    protected String mess;
    protected Peer peer;
    public Message(Peer sender, String message){
        this.mess = message;
        this.peer = sender;
    }
    
    public String getMessage(){
        return mess;
    }
    public String getName(){
        return peer.getUsername();
    }
    
}
