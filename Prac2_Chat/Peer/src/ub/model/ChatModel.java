package ub.model;

import ub.model.workers.AttemptingToReconnect;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map.Entry;
import ub.common.Message;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import ub.common.GroupReference;
import ub.common.IPeer;
import ub.common.IServer;
import ub.common.UserInUseException;
import ub.exceptions.WrongAdresseeException;
import ub.model.Chat.ChatListener;
import ub.model.Group.GroupListener;
import ub.model.workers.NotifyConnectionDown;
import ub.model.workers.PingServer;

/**
 *
 * @author Pablo
 */
public class ChatModel implements ChatModelServices, AttemptingToReconnect.IReconnect{
    // Server variables
    private String IP;
    private int port;
    
    //
    
    private final ChatRoomListener listener;
    private String myUsername;
    private Peer myPeer;
    private IServer server;
    public ConcurrentHashMap<String,IPeer> connections;                         // All the connections         
    public ConcurrentHashMap<String, Chat> chats;                               // Chats
    public ConcurrentHashMap<GroupReference, Group> groups;                     // Groups
    private final ExecutorService executor;
    
    
    public ChatModel(ChatRoomListener listener) {
        this.executor = Executors.newFixedThreadPool(10);
        this.listener = listener;
        this.connections = new ConcurrentHashMap<>();
        this.chats = new ConcurrentHashMap<>();
        this.groups = new ConcurrentHashMap<>();
        
    }
    
    //                 //
    /* General methods */
    //                 //
    
    void userConnected(String username, IPeer peer) {
        connections.put(username, peer);
        listener.onMemberConnected(username);
    }
    
    void userDisconnected(String username){
        connections.remove(username);
        listener.onMemberDisconnected(username);
        removeItOfAllGroups(username);
        
        
    }

    private synchronized void notifyDisconnection(String username) {
        // Due to the ammount of threads created on this function
        // And the possible recursivity, it's important to synchronize this
        // function and only allow 1 thread to be on it.

        connections.remove(username);
        listener.onMemberDisconnected(username);
        removeItOfAllGroups(username);
        
        boolean b = checkServerState();
        if (b) return;
        //This method will be working just when server is
        //Down.
        
        for (Entry<String, IPeer> e: connections.entrySet()) {
            if (e.getKey().equals(myUsername)) continue;
            Runnable worker = new NotifyConnectionDown(this, username, e.getKey(),e.getValue());
            executor.execute(worker);
        }
        try {
            // Join threads
            executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            System.err.println("Interrupted");
        }
    }

    public void disconnect() {
        notifyDisconnection(myUsername);
    }

    public void setConnections(ConcurrentHashMap<String,IPeer> c) throws RemoteException{
        
        for(String s: c.keySet()) {
            if(s.equals(myUsername) || this.connections.containsKey(s))continue;
            listener.onMemberConnected(s);
            //You may also notify other connections you are here.
            c.get(s).userConnect(myUsername, myPeer);
        }
        this.connections = c;
            
    }
    
    void recieveServerDownFlag() {
        checkServerState();
    }
    
    @Override
    public void register(String IP, int port, String username) throws NotBoundException, MalformedURLException, RemoteException, UserInUseException{
        this.IP = IP;
        this.port = port;
        myUsername = username;
        myPeer = new Peer(username,this);
        server = (IServer) Naming.lookup("rmi://"+IP+":"+port+"/Server");
        setConnections(server.registryUser(username, (IPeer)myPeer));
        listener.onServerUp();
        new Thread(new PingServer(this,server)).start();
    }
    
    public ArrayList<String> getConnectedClients(){
        ArrayList<String> a = new ArrayList<>();
        for (String s : connections.keySet()){
            a.add(s);
        }
        return a;
    }
    
    //                //
    /* Server methods */
    //                //
    private boolean checkServerState() {
        if (server == null) return false;
        
        try {
            // Check server state
            server.ping();
            return true;
        } catch (RemoteException ex) {
            server = null;
            return false;
        }
    }
    
    @Override
    public void notifyServerDown(){
        this.server = null;
        listener.onServerDown();
        new Thread(new AttemptingToReconnect(this, IP, port, myUsername)).start();
    }
    
    //                  //
    /* For single chats */
    //                  //
    
    public void writeMessage(String adressee, String message) throws WrongAdresseeException{
        Message m = new Message(myUsername,message);
        
        // Check if adressee exists.
        if (connections.get(adressee)==null) throw new WrongAdresseeException();
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
            // Dont exist. Create a new Chat
            c = createChat(sender);
        }
        c.reciveMessage(m);
    }
    
    public Chat createChat(String username){
        ChatListener l = listener.onNewChatCreated(username);
        Chat c = new Chat(this, l, username);
        chats.put(username, c);
        return c;
    }
    
    public void userIsTypingSender(String username){
        Chat c = chats.get(username);
        if (c != null){
            c.userIsTypingSender(myUsername);
        }
    }
    
    public void userIsTypingReceiver(String username) {
        Chat c = chats.get(username);
        if (c != null){
            c.userIsTypingReceiver();
        }
    }
    
    
    //            //
    /* For groups */
    //            //
    
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
    
    public void addGroupMember(GroupReference gref, String username){
        groups.get(gref).addMember(username);
    }
    
    public void removeGroupMember(GroupReference gref, String username){
        groups.get(gref).removeMember(username);
    }
    
    private void removeItOfAllGroups(String username) {
        for(Group g: groups.values()) g.removeMember(username);
    }
    
    //                             //
    /* Utilities for lower classes */
    //                             //

    @Override
    public IPeer getIPeerByName(String username) {
        return connections.get(username);
    }

    @Override
    public void notifyDisconnectedClient(String username) {
        notifyDisconnection(username);
    }

    @Override
    public String getMyUserName() {
        return myUsername;
    }





    
    public interface ChatRoomListener{
        public ChatListener onNewChatCreated(String username);
        public GroupListener onNewGroupCreated(GroupReference gref, ArrayList<String> members, String groupName);
        public void onMemberConnected(String username);
        public void onMemberDisconnected(String username);
        public void onServerDown();
        public void onServerUp();
    }
    

   
}
