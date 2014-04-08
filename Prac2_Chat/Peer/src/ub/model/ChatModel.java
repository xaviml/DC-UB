package ub.model;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;
import ub.common.Message;
import java.util.concurrent.ConcurrentHashMap;
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
public class ChatModel implements ChatModelServices{
    
    private final ChatRoomListener listener;
    private String myUsername;
    private Peer myPeer;
    private IServer server;
    public ConcurrentHashMap<String,IPeer> connections;
    public ConcurrentHashMap<String, Chat> chats;
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
    
    public void writeMessage(String adressee, String message){
        Message m = new Message(myUsername,message);
        
        // Check if adressee exists.
        if (connections.get(adressee)==null) throw new WrongAdreseeException();
        Chat c = chats.get(adressee);
        
        // Check if we already have a chat with the client
        if (c == null){
            // If not, create a new one.
            c = createChat(adressee);
        }
        c.writeMessage(m);
    }
    
    public void recieveMessage(Message m) throws RemoteException{
        String sender = m.getUsername();
        Chat c = chats.get(sender);
        if (c == null){
            // Doesn't exist. Create a new Chat
            c = createChat(sender);
        }
        c.writeMessage(m);
    }
    
    
    public void addGroup(GroupListener ls, ArrayList<String> members, String groupName, GroupReference gref){
        if (ls == null)
            ls = listener.onNewGroupCreated(gref, members, groupName);
        
        // Create the IPeer list, in order to create the Group
        Group g = new Group(this, ls,members,groupName,gref);
        
        // Check gref. If function is called internally it would be null.
        // Else, if function is called externally it would already have a gref.
        if (gref == null) gref = g.getRef();
        groups.put(gref, g);
    }

    public void changeGroupName(GroupReference gref, String newName){
        groups.get(gref).setName(newName);
        
    }
    
    public void writeMessage(GroupReference gref, String message){
        Group g = groups.get(gref);
        g.writeMessage(new Message(myUsername, message));
    }
    
    public void recieveMessage(GroupReference gref, Message message){
        groups.get(gref).reciveMessage(message);
    }
    
    
    public Chat createChat(String username){
        ChatListener l = listener.onNewChatCreated(username);
        Chat c = new Chat(this, l, new ArrayList<>(Arrays.asList(myUsername, username)));
        chats.put(username, c);
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

    @Override
    public IPeer getIPeerByName(String username) {
        return connections.get(username);
    }

    @Override
    public void notifyDisconnectedClient(String username) {
        notifyDisconnection(username);
    }
    
    
    public interface ChatRoomListener{
        public ChatListener onNewChatCreated(String username);
        public GroupListener onNewGroupCreated(GroupReference gref, ArrayList<String> members, String groupName);
        public void onMemberConnected(String username);
        public void onMemberDisconnected(String username);
    }
    

   
}
