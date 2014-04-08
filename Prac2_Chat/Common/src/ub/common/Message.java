/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.common;

import java.io.Serializable;

/**
 *
 * @author Pablo
 */
public class Message implements Serializable{
    private String user;
    private String message;
    
    
    public Message(String p, String s){
        this.user = p;
        this.message = s;
    }
    
    public String getMessage(){
        return message;
    }

    public String getUsername(){
        return user;
    }

    @Override
    public String toString() {
        return "Message{" + "user=" + user + ", message=" + message + '}';
    }

    
}
