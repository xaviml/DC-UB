/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model.workers;

import java.rmi.RemoteException;
import ub.common.IPeer;
import ub.model.ChatModelServices;

/**
 *  This class is an executor.
 * Just notify another connection that someone has disconnected.
 * 
 * @author kirtash
 */
public class NotifyConnectionDown implements Runnable{
    IPeer peer;
    String removedUser;
    String user;
    ChatModelServices services;
    
    public NotifyConnectionDown(ChatModelServices services, String removeduser, String username, IPeer peer){
        this.user = username;
        this.removedUser = removeduser;
        this.peer = peer;
        this.services = services;
    }
    @Override
    public void run() {
        try {
            //System.out.println("Notifying: "+user+". "+removedUser+" is gone.");
            peer.userDisconnect(removedUser);
        } catch (RemoteException ex) {
            // Disconnectception!
            // That's why notifyDisconnectedClient is a synchronized function...
            services.notifyDisconnectedClient(user);
        }
    }
    
}
