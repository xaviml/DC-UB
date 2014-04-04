/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import ub.common.Message;
import java.util.concurrent.ConcurrentHashMap;
import ub.common.GroupReference;
import ub.common.IPeer;
import ub.model.Chat.ChatListener;

/**
 *
 * @author Pablo
 */
public class ChatModel {
    
    private ChatRoomListener listener;
    private final IPeer myRemotePeer;
    public ConcurrentHashMap<String,IPeer> members;
    public ConcurrentHashMap<IPeer, Chat> chats;
    public ConcurrentHashMap<GroupReference, Group> groups;
    
    public ChatModel(ChatRoomListener listener, IPeer myRemotePeer){
        this.listener = listener;
        this.myRemotePeer = myRemotePeer;
        this.members = new ConcurrentHashMap<>();
        this.chats = new ConcurrentHashMap<>();
        this.groups = new ConcurrentHashMap<>();
        
    }
    
    public ArrayList<String> getConnectedClients(){
        ArrayList<String> a = new ArrayList<>();
        for (String s : members.keySet()){
            a.add(s);
        }
        return a;
    }
    
    
    /**
     * For single chats
     * @param username
     * @param message
     * @return 
     */
    public boolean writeMessage(String username, String message) throws RemoteException{
        Message m = new Message(myRemotePeer,message);
        IPeer adressee = members.get(username);
        
        // Check if adressee exists.
        if (adressee == null) return false;
        Chat c = chats.get(adressee);
        
        // Check if we already have a chat with the client
        if (c == null){
            // If not, create a new one.
            c = createChat(adressee, username);
        }
        c.writeMessage(m);
        return adressee.writeMessage(m);
    }
    
    /**
     * For single chats
     * @param m
     * @return
     * @throws RemoteException 
     */
    public boolean recieveMessage(Message m) throws RemoteException{
        IPeer sender = m.getIPeer();
        Chat c = chats.get(sender);
        if (c == null){
            // Doesn't exist. Create a new Chat
            String username = sender.getUsername();
            c = createChat(sender, username);
        }
        c.writeMessage(m);
        return false;
    }
    
    
    public Chat createChat(IPeer peer, String username){
        ChatListener l = listener.onNewChatCreated(username);
        return new Chat(l, peer);
    }

    void userConnected(String username, IPeer peer) {
        members.put(username, peer);
    }
    
    
    public interface ChatRoomListener{
        public ChatListener onNewChatCreated(String username);
    }
   
}