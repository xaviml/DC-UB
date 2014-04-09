/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model.workers;

import java.rmi.RemoteException;
import ub.common.IServer;
import ub.model.ChatModelServices;

/**
 *
 * @author kirtash
 */
public class PingServer implements Runnable{
    private final IServer server;
    private final ChatModelServices services;
    public PingServer(ChatModelServices services, IServer server){
        this.server = server;
        this.services = services;
    }
    @Override
    public void run() {
        while(true){
            try {
                server.ping();
                
                synchronized (this){
                    this.wait(5000);
                }
                
            } catch (RemoteException ex) {
                services.notifyServerDown();
                break;
            } catch (InterruptedException ex) {
                System.err.println("Interrupted");
            }
        }
    }
    
}
