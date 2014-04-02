/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import ub.common.IPeer;

/**
 *
 * @author Pablo
 */
public class Message {
    public String mess;
    public IPeer peer;
    public Message(IPeer sender, String message){
        this.mess = message;
        this.peer = sender;
    }
    
}
