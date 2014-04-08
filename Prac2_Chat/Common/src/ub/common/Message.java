/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.common;

import java.io.Serializable;
import ub.common.IPeer;

/**
 *
 * @author Pablo
 */
public class Message implements Serializable{
    private IPeer user;
    private String message;
    
    
    public Message(IPeer p, String s){
        this.user = p;
        this.message = s;
    }
    
    public String getMessage(){
        return message;
    }

    public IPeer getIPeer(){
        return user;
    }

    @Override
    public String toString() {
        return "Message{" + "user=" + user + ", message=" + message + '}';
    }

    
}
