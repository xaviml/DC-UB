/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model.workers;

import static java.lang.Thread.sleep;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import ub.common.UserInUseException;

/**
 *
 * @author kirtash
 */
public class AttemptingToReconnect implements Runnable{
    private final IReconnect register;
    private final String IP;
    private final int port;
    private final String username;
    private boolean connected;
    
    
    public AttemptingToReconnect(IReconnect register, String IP, int port, String username){
        this.IP = IP;
        this.port = port;
        this.register = register;
        this.username = username;
        this.connected = false;
    }
    
    
    @Override
    public void run() {
        while (!connected){
            try {
                register.register(IP, port, username);
                connected = true;
            } catch (NotBoundException | MalformedURLException | RemoteException | UserInUseException ex) {
                try {
                    synchronized(this){
                        sleep(5000);
                    }
                } catch (InterruptedException ex1) {}
            }
        }
    }
    
    
    public interface IReconnect{
        public void register(String IP, int port, String username) throws NotBoundException, MalformedURLException, RemoteException, UserInUseException;
    }
    
}
