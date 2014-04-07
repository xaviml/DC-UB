/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import ub.common.IPeer;
import ub.common.IServer;

/**
 *
 * @author zenbook
 */
public class Server extends UnicastRemoteObject implements IServer{

    public ConcurrentHashMap<String,IPeer> connections;
    public Server() throws RemoteException {
        
    }
    
    @Override
    public ConcurrentHashMap<String,IPeer> registryUser(String username, IPeer peer) throws RemoteException {
        System.out.println(peer.getUsername()+" registred");
        connections.put(username, peer);
        return connections;
    }

    @Override
    public void unregistryUser(String username) {
        connections.remove(username);
    }

    @Override
    public IPeer getUser(String user) {
        return connections.get(user);
    }
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if(args.length != 1) {
            System.out.println("Execute: java -jar <port>");
            System.exit(0);
        }
        int port = 0;
        try{
             port = Integer.parseInt(args[0]);
        }catch(NumberFormatException ex) {
            System.out.println("Port must be a integer");
            System.exit(0);
        }
        
        if(port<1024 || port > 65535) {
            System.out.println("Port must be between 1024 and 65535, both included");
            System.exit(0);
        }
        
        
        
        try {
            LocateRegistry.createRegistry(port);
            Server server = new Server();
            
            Naming.rebind("rmi://localhost:"+port+"/Server", server);
        } catch (RemoteException | MalformedURLException ex) {
            System.out.println("Impossible to connect with rmiregistry");
        }
    }

    @Override
    public ArrayList<IPeer> getUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
