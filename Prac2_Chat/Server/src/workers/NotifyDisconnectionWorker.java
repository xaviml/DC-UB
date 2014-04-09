/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package workers;

import java.rmi.RemoteException;
import ub.common.IPeer;

/**
 *
 * @author kirtash
 */
public class NotifyDisconnectionWorker implements Runnable{
        IPeer p;
        String s;
        String rm;
        DisconnectedList service;
        
        public NotifyDisconnectionWorker(DisconnectedList disconnected, String removedUser, String s, IPeer p){
            this.service = disconnected;
            this.p = p;
            this.s = s;
            this.rm = removedUser;
        }
        @Override
        public void run() {
            try {
                p.userDisconnect(rm);
            } catch (RemoteException ex) {
                service.addDisconnected(s);
            }
        }
    } 