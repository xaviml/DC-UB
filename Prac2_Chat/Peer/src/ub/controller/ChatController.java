/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.controller;

import ub.model.Chat;
import ub.model.Peer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.IServer;

/**
 *
 * @author Xavi Moreno
 */
public class ChatController {
    private Peer myPeer;
    private ArrayList<Chat> chats;
    private IServer server;
    
    private OnPeerListener peerListener;
    private OnServerListener serverListener;
    
    public ChatController(String IP, String username, OnPeerListener peerListener, OnServerListener serverListener) {
        this.peerListener = peerListener;
        this.serverListener = serverListener;
        
        this.chats = new ArrayList<>();
        //Here we must create and registry a new Peer.
        try {
            myPeer = new Peer(username);
            IServer ser = (IServer) Naming.lookup("rmi://localhost:1099/Server");
            ser.registryUser(myPeer);
            //dateServer.registryUser(myPeer);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public interface OnPeerListener {
        
    }
    
    public interface OnServerListener {
        
    }
}
