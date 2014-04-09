/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package server;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import ub.common.IPeer;
import workers.DisconnectedList;
import workers.NotifyDisconnectionWorker;
import workers.PingWorker;

/**
 *
 * @author kirtash
 */
public class Pinger implements Runnable, DisconnectedList {
    private final ArrayList<String> disconnected;
    private final ServerServices services;
    private final ExecutorService executor;
    private final boolean working;
    
    public Pinger(ServerServices services){
        this.working = true;
        this.executor = Executors.newFixedThreadPool(10);
        this.services = services;
        this.disconnected = new ArrayList<>();
    }
    
    @Override
    public void run() {
        
        while(working){
            try {
                
                // Check off connections
                for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
                    Runnable worker = new PingWorker(this, e.getKey(),e.getValue());
                    executor.execute(worker);
                }
                // Join threads
                executor.awaitTermination(5000, TimeUnit.MILLISECONDS);

                // Do the propper disconnections
                for (String s: disconnected){
                    services.disconnectClient(s);
                }
                
                // Do the callback.
                for(String s: disconnected){
                    for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
                        Runnable worker = new NotifyDisconnectionWorker(this, s,e.getValue());
                        executor.execute(worker);
                    }
                    executor.awaitTermination(1, TimeUnit.MINUTES);
                }
                disconnected.clear();
                synchronized(this){
                    this.wait(3000);
                }
            } catch (InterruptedException ex) {
                System.err.println("Interrupted");
            }
        }
        executor.shutdown();
    }

    @Override
    public void addDisconnected(String s) {
        synchronized(disconnected){
            disconnected.add(s);
        }
    }
    
    public interface ServerServices{
        public ConcurrentHashMap<String,IPeer> getConnections();
        public void disconnectClient(String s);
    }
}
