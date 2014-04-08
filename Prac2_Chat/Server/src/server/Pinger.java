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
        while(true){
            try {
                ExecutorService executor = Executors.newFixedThreadPool(10);
                
                // Check off connections
                for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
                    Runnable worker = new WorkerPing(e.getKey(),e.getValue());
                    executor.execute(worker);
                }
                executor.shutdown();
                while (!executor.isTerminated()) {}
                
                // Disconnect them, and do callback.
                for(String s: disconnected){
                    services.disconnectClient(s);
                    for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
                        Runnable worker = new WorkerNotifyDisconnect(s,e.getValue());
                        executor.execute(worker);
                    }
                    executor.shutdown();
                    while (!executor.isTerminated()) {}
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
