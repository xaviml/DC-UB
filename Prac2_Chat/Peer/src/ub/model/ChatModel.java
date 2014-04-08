package ub.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map.Entry;
import ub.common.Message;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.GroupReference;
import ub.common.IPeer;
import ub.common.IServer;
import ub.common.InvalidUserNameException;
import ub.exceptions.WrongAdreseeException;
import ub.model.Chat.ChatListener;
import ub.model.Group.GroupListener;

/**
 *
 * @author Pablo
 */
public class ChatModel {
    
    private final ChatRoomListener listener;
    private String myUsername;
    private Peer myPeer;
    private IServer server;
    public ConcurrentHashMap<String,IPeer> connections;
    public ConcurrentHashMap<IPeer, Chat> chats;
    public ConcurrentHashMap<GroupReference, Group> groups;
    
    public ChatModel(ChatRoomListener listener){
        
        this.listener = listener;
        this.connections = new ConcurrentHashMap<>();
        this.chats = new ConcurrentHashMap<>();
        this.groups = new ConcurrentHashMap<>();
        
    }
    
    // Tested (OK)
    public void register(String IP, int port, String username) throws NotBoundException, MalformedURLException, RemoteException, InvalidUserNameException{
        myUsername = username;
        myPeer = new Peer(username,this);
        server = (IServer) Naming.lookup("rmi://"+IP+":"+port+"/Server");
        setConnections(server.registryUser(username, (IPeer)myPeer));
    }
    
    
    public ArrayList<String> getConnectedClients(){
        ArrayList<String> a = new ArrayList<>();
        for (String s : connections.keySet()){
            a.add(s);
        }
        return a;
    }
    
    
    /**
     * For single chats
     * @param username
     * @param message 
     * @throws ub.exceptions.WrongAdreseeException 
     */
    public void writeMessage(String username, String message) throws WrongAdreseeException{
        Message m = new Message(myUsername,message);
        IPeer adressee = connections.get(username);
        
        // Check if adressee exists.
        if (adressee == null) throw new WrongAdreseeException();
        Chat c = chats.get(adressee);
        
        // Check if we already have a chat with the client
        if (c == null){
            // If not, create a new one.
            c = createChat(adressee, username);
        }
        c.writeMessage(m);
        try {
            adressee.writeMessage(m);
        } catch (RemoteException ex) {
            notifyDisconnection(username);
        }
    }
    
    /**
     * For single chats
     * @param m
     * @return
     * @throws RemoteException 
     */
    public boolean recieveMessage(Message m) throws RemoteException{
        IPeer sender = connections.get(m.getUsername());
        Chat c = chats.get(sender);
        if (c == null){
            // Doesn't exist. Create a new Chat
            String username = sender.getUsername();
            c = createChat(sender, username);
        }
        c.writeMessage(m);
        return false;
    }
    
    
    public void addGroup(GroupListener ls, ArrayList<String> members, String groupName, GroupReference gref){
        if (ls == null)
            ls = listener.onNewGroupCreated(gref, members, groupName);
        
        // Create the IPeer list, in order to create the Group
        ArrayList<IPeer> peers = new ArrayList<>();
        for(String s: members) peers.add(connections.get(s));
        Group g = new Group(ls,peers,groupName,gref);
        
        // Check gref. If function is called internally it would be null.
        // Else, if function is called externally it would already have a gref.
        if (gref == null) gref = g.getRef();
        groups.put(gref, g);
    }

    public void changeGroupName(GroupReference gref, String newName){
        groups.get(gref).setName(newName);
        
    }
    
    public void writeGroupMessage(GroupReference gref, String message){
        Group g = groups.get(gref);
        try {
            g.writeMessage(new Message(myUsername, message));
        } catch (RemoteException ex) {
            System.err.println("Gay");
        }
    }
    
    public void recieveGroupMessage(GroupReference gref, Message message){
        
    }
    
    
    public Chat createChat(IPeer peer, String username){
        ChatListener l = listener.onNewChatCreated(username);
        Chat c = new Chat(l,peer);
        chats.put(peer, c);
        return c;
    }

    void userConnected(String username, IPeer peer) {
        connections.put(username, peer);
        listener.onMemberConnected(username);
    }
    
    void userDisconnected(String username){
        connections.remove(username);
        listener.onMemberDisconnected(username);
    }

    private void notifyDisconnection(String username) {
        connections.remove(username);
        listener.onMemberDisconnected(username);
        try {
            server.unregistryUser(username);
        } catch (RemoteException ex) {
            System.err.println("Server disconnected");
        }
        for (Entry<String,IPeer> e: connections.entrySet()){
            if (!e.getKey().equals(myUsername))try {
                e.getValue().userDisconnect(username);
            } catch (RemoteException ex) {
                // Bad luck.. While we were notifying a disconnection
                // Another user was also disconnected. Notify this too.. :/
                notifyDisconnection(e.getKey());
            }
        }
    }

    public void disconnect() {
        notifyDisconnection(myUsername);
    }

    public void setConnections(ConcurrentHashMap<String,IPeer> c) throws RemoteException{
        this.connections = c;
        for(String s: c.keySet()) {
            if(!s.equals(myUsername)) {
                listener.onMemberConnected(s);
            }
            //You may also notify other connections you are here.
            c.get(s).userConnect(myUsername, myPeer);
        }
            
    }
    
    
    public interface ChatRoomListener{
        public ChatListener onNewChatCreated(String username);
        public GroupListener onNewGroupCreated(GroupReference gref, ArrayList<String> members, String groupName);
        public void onMemberConnected(String username);
        public void onMemberDisconnected(String username);
    }
    

   
}
