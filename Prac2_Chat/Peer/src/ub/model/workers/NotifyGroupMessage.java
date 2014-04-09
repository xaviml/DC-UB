/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model.workers;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.GroupReference;
import ub.common.IPeer;
import ub.common.Message;
import ub.model.ChatModelServices;

/**
 *
 * @author kirtash
 */
public class NotifyGroupMessage implements Runnable{
    private IPeer adreesse;
    private String username;
    private Message message;
    private GroupReference gref;
    private ChatModelServices services;
    
    public NotifyGroupMessage(ChatModelServices services, GroupReference gref, Message m, IPeer adreesse, String username){
        this.gref = gref;
        this.message = m;
        this.username = username;
        this.adreesse = adreesse;
        this.services = services;
    }
    @Override
    public void run() {
        try {
            adreesse.writeMessage(gref, message);
        } catch (RemoteException ex) {
            services.notifyDisconnectedClient(username);
        }
        
    }
    
}
