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
public class PingWorker implements Runnable{
        IPeer p;
        String s;
        DisconnectedList service;
        public PingWorker(DisconnectedList service, String s, IPeer p){
            this.service = service;
            this.p = p;
            this.s = s;
        }
        @Override
        public void run() {
            try {
                p.ping();
            } catch (RemoteException ex) {
                service.addDisconnected(s);
            }
        }
    }
