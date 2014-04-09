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
import server.Pinger.ServerServices;
import ub.common.IPeer;
import ub.common.IServer;
import ub.common.UserInUseException;

/**
 *
 * @author zenbook
 */
public class Server extends UnicastRemoteObject implements ServerServices, IServer{
    public ConcurrentHashMap<String,IPeer> connections;
    
    
    public Server() throws RemoteException {
        connections = new ConcurrentHashMap<>();
        new Thread(new Pinger(this)).start();
    }
    
    @Override
    public ConcurrentHashMap<String,IPeer> registryUser(String username, IPeer peer) throws RemoteException, UserInUseException {
        if (connections.get(username)!=null) throw new UserInUseException();
        System.out.println(username+" registred");
        connections.put(username, peer);
        return connections;
    }

    @Override
    public void unregistryUser(String username) {
        connections.remove(username);
        System.out.println("Disconnected "+username);
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
    public ArrayList<String> getUsers() {
        ArrayList<String> a = new ArrayList();
        for (String s : connections.keySet()) a.add(s);
        return a;
    }

    @Override
    public ConcurrentHashMap<String, IPeer> getConnections() {
        return connections;
    }

    @Override
    public void disconnectClient(String s) {
        connections.remove(s);
        // May notify clients
    }

    @Override
    public void ping() throws RemoteException {}
    
}
