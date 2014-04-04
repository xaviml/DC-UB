/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package deprecated;

import ub.common.IPeer;

/**
 *
 * @author Pablo
 */
public class Writer implements Runnable{
    private final IPeer reciever;
    private final IPeer sender;
    private final Message message;
    
    public Writer(IPeer sender, IPeer reciever, Message message){
        this.reciever = reciever;
        this.sender = sender;
        this.message = message;
    }
    
    @Override
    public void run(){
        //reciever.writeMessage(sender, message);
        
    }
    
}
