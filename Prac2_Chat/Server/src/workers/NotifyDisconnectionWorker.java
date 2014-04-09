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
        DisconnectedList service;
        
        public NotifyDisconnectionWorker(DisconnectedList disconnected, String s, IPeer p){
            this.service = disconnected;
            this.p = p;
            this.s = s;
        }
        @Override
        public void run() {
            try {
                p.userDisconnect(s);
            } catch (RemoteException ex) {
                service.addDisconnected(s);
            }
        }
    } 