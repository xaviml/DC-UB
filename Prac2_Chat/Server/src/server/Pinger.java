/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import ub.common.IPeer;

/**
 *
 * @author kirtash
 */
public class Pinger implements Runnable{
    private final ArrayList<String> disconnected;
    ServerServices services;
    
    public Pinger(ServerServices services){
        this.services = services;
        this.disconnected = new ArrayList<>();
    }
    
    @Override
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        while(true){
            try {
                
                
                // Check off connections
                System.out.println("Check disponibility");
                for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
                    Runnable worker = new WorkerPing(e.getKey(),e.getValue());
                    executor.execute(worker);
                }
                executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                System.out.println("Finished all threads");
                
                
                // Do the callback.
                for(String s: disconnected){
                    System.out.println("Notifying "+s);
                    for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
                        Runnable worker = new WorkerNotifyDisconnect(s,e.getValue());
                        executor.execute(worker);
                    }
                    executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
                    System.out.println("Finished all threads");
                }
                disconnected.clear();
                synchronized(this){
                    System.err.println("Sleeping");
                    this.wait(3000);
                }
            } catch (InterruptedException ex) {
                System.err.println("Interrupted");
            }
        }
    }
    
    public interface ServerServices{
        public ConcurrentHashMap<String,IPeer> getConnections();
        public void disconnectClient(String s);
    }
    
    
    public class WorkerPing implements Runnable{
        IPeer p;
        String s;
        public WorkerPing(String s, IPeer p){
            this.p = p;
            this.s = s;
        }
        @Override
        public void run() {
            try {
                p.ping();
            } catch (RemoteException ex) {
                services.disconnectClient(s);
                synchronized(disconnected){
                    disconnected.add(s);
                }
            }
        }
    }
        
    public class WorkerNotifyDisconnect implements Runnable{
        IPeer p;
        String s;
        public WorkerNotifyDisconnect(String s, IPeer p){
            this.p = p;
            this.s = s;
        }
        @Override
        public void run() {
            try {
                p.userDisconnect(s);
            } catch (RemoteException ex) {
                synchronized(disconnected){
                    disconnected.add(s);
                }
            }
        }
    }   
}
