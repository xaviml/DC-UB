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
    private ExecutorService executor;
    private final boolean working;
    private final boolean newDisconnectedFlag;
    
    public Pinger(ServerServices services){
        this.newDisconnectedFlag = false;
        this.working = true;
        this.services = services;
        this.disconnected = new ArrayList<>();
    }
    
    @Override
    public void run() {
        
        while(working){
            try {
                // Do the propper disconnections
                notifyDisconnections();
                
                // Wait 3sec
                synchronized(this){
                    this.wait(3000);
                }
                
                // Ping all connections
                pingConnections();
                
            } catch (InterruptedException ex) {/*Server added a disconnection!*/}
        }
    }

    public void notifyDisconnections() throws InterruptedException{
        ArrayList<String> cpy = (ArrayList<String>)disconnected.clone();
            for (String s: cpy){
                services.disconnectClient(s);
            }

            // Do the callback.
            for(String s: cpy){
                this.executor = Executors.newFixedThreadPool(10);
                for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
                    Runnable worker = new NotifyDisconnectionWorker(this, s, e.getKey(), e.getValue());
                    executor.execute(worker);
                }
                executor.shutdown();
                executor.awaitTermination(3, TimeUnit.SECONDS);
                disconnected.remove(s);
            }
        disconnected.clear();
    }
    
    
    public void pingConnections() throws InterruptedException{
        this.executor = Executors.newFixedThreadPool(10);
        // Check off connections
        for (Entry<String, IPeer> e: services.getConnections().entrySet()) {
            Runnable worker = new PingWorker(this, e.getKey(),e.getValue());
            executor.execute(worker);
        }
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }
    
    @Override
    public void addDisconnected(String s) {
        synchronized(disconnected){
            if (!disconnected.contains(s)) disconnected.add(s);
        }
    }
    
    public interface ServerServices{
        public ConcurrentHashMap<String,IPeer> getConnections();
        public void disconnectClient(String s);
    }
}
