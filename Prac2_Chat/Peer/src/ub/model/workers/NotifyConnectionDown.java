/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model.workers;

import java.rmi.RemoteException;
import ub.common.IPeer;
import ub.model.ChatModelServices;

/**
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
            peer.userDisconnect(removedUser);
        } catch (RemoteException ex) {
            services.notifyDisconnectedClient(user);
        }
    }
    
}
