/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ub.common.IPeer;

/**
 *
 * @author Xavi Moreno
 */
public class Chat {
    public String name; //This is de chat name
    public ArrayList<IPeer> peers;
    public final ArrayList<Message> chat; //set of conversations
    public long idChat;
    public boolean groupFlag = false;
    public ChatListener listener;
    
    
    public Chat(long idChat){
        this.idChat = idChat;        
        this.chat = new ArrayList<>();
        this.peers = new ArrayList<>();
    }
    
    public Chat(long idChat, ChatListener chat) {
        this.idChat = idChat;
        this.listener = chat;
        this.chat = new ArrayList<>();
        this.peers = new ArrayList<>();
    }
 
    public boolean addUser(IPeer peer) {
        peers.add(peer);
        if (listener != null) listener.onClientJoinListener(peer);
        return true;
    }
    

    public void writeMessage(Message message) throws RemoteException{
        synchronized(chat){
            chat.add(message);
        }
        ExecutorService executor = Executors.newFixedThreadPool(5);
        for (IPeer p : peers) {
            if (p.equals(this)) continue; // Don't send it to yourself
            executor.execute(new Writer(message.peer, p, message));
        }
    }

    void setListener(ChatListener listener) {
        this.listener = listener;
    }

    
    
    public interface ChatListener{
        public void onNewMessageListener(Message m);
        public void onClientLeaveListener(IPeer peer);
        public void onClientJoinListener(IPeer peer);
    }
}
