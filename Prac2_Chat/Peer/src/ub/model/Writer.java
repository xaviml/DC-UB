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
public class Writer implements Runnable{
    private final IPeer reciever;
    private final IPeer sender;
    private final String message;
    
    public Writer(IPeer sender, IPeer reciever, String message){
        this.reciever = reciever;
        this.sender = sender;
        this.message = message;
    }
    
    @Override
    public void run(){
        //reciever.writeMessage(sender, message);
        
    }
    
}
