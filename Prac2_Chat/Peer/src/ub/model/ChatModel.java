package ub.model;

import ub.model.workers.AttemptingToReconnect;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map.Entry;
import ub.common.Message;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ub.common.IPeer;
import ub.common.IServer;
import ub.common.UserInUseException;
import ub.exceptions.WrongAdresseeException;
import ub.model.Chat.ChatListener;
import ub.model.Group.GroupListener;
import ub.model.workers.NotifyConnectionDown;
import ub.model.workers.NotifyGroup;
import ub.model.workers.PingServer;

/**
 *  This is the main model class. Here there are all the relevant objects of the
 * program. 
 * 
 * @author Pablo
 */
public class ChatModel implements ChatModelServices, AttemptingToReconnect.IReconnect{
    // Server variables
    private String IP;
    private int port;
    
    //
    
    // Threads
    private Thread serverDownThread;
    private Thread serverPingThread;
    
    
    private final ChatRoomListener listener;
    private String myUsername;
    private Peer myPeer;
    private IServer server;
    public ConcurrentHashMap<String,IPeer> connections;                         // All the connections         
    public ConcurrentHashMap<String, Chat> chats;                               // Chats
    public ConcurrentHashMap<String, Group> groups;                             // Groups
    private ExecutorService executor;
    
    
    public ChatModel(ChatRoomListener listener) {
        this.executor = null;
        this.listener = listener;
        this.connections = new ConcurrentHashMap<>();
        this.chats = new ConcurrentHashMap<>();
        this.groups = new ConcurrentHashMap<>();
        
    }
    
    //                 //
    /* General methods */
    //                 //
    
    void userConnected(String username, IPeer peer) {
        if (connections.containsKey(username)) return;
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
        
        try {
            if(server==null) throw new RemoteException();
            server.unregistryUser(username);
        } catch (RemoteException ex) {
            //This method will be working just when server is
            //Down.
            this.executor = Executors.newFixedThreadPool(10);
            for (Entry<String, IPeer> e: connections.entrySet()) {
                if (e.getKey().equals(myUsername)) continue;
                Runnable worker = new NotifyConnectionDown(this, username, e.getKey(),e.getValue());
                executor.execute(worker);
            }
            executor.shutdown();
        }

    }

    public void disconnect() {
        if (serverDownThread != null) serverDownThread.interrupt();
        if (serverPingThread != null) serverPingThread.interrupt();
        notifyDisconnection(myUsername);
    }

    public void setConnections(ConcurrentHashMap<String,IPeer> c) throws RemoteException{
        
        for(String s: c.keySet()) {
            if(s.equals(myUsername) || this.connections.containsKey(s)) continue;
            listener.onMemberConnected(s);
            //You may also notify other connections you are here.
            c.get(s).userConnect(myUsername, myPeer);
        }
        this.connections = c;
            
    }
    
    void recieveServerDownFlag() {
        this.listener.onServerDown();
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
        serverPingThread = new Thread(new PingServer(this,server));
        serverPingThread.start();
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
    
    @Override
    public void notifyServerDown(){
        this.server = null;
        listener.onServerDown();
        serverDownThread = new Thread(new AttemptingToReconnect(this, IP, port, myUsername));
        serverDownThread.start();
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
    
    public void addGroup(ArrayList<String> members, String groupName, String gref){
//        if (groups.get(gref)!= null) return; // This group already exist!
        boolean rec = true;
        if(gref== null){
            rec = false;
            Date d = new Date();
            gref = myUsername+(d.getTime());
        }
        GroupListener ls = listener.onNewGroupCreated(gref, members, groupName);
        Group g = new Group(this, ls, members, groupName, gref);
        
        // Check gref. If function is called internally it would be null.
        // Else, if function is called externally it would already have a gref.
        groups.put(gref, g);
        if (!rec){
            this.executor = Executors.newFixedThreadPool(10);
            members.add(myUsername);
            for(String s: members) {
                if(s.equals(myUsername)) continue;
                executor.execute(new Thread(new NotifyGroup(this, gref, groupName, members, connections.get(s), myUsername)));
            }
            executor.shutdown();
        }
    }
        
    public void changeGroupName(String gref, String newName){
        groups.get(gref).setName(newName);
        
    }
    
    public void writeMessageGroup(String gref, String message){
        Group g = groups.get(gref);
        g.writeMessage(new Message(myUsername, message));
    }
    
    public void recieveMessageGroup(String gref, Message message){
        groups.get(gref).reciveMessage(message);
    }
    
    public void addGroupMember(String gref, String username){
        groups.get(gref).addMember(username);
    }
    
    public void addGroupMemberAndNotify(String gref, ArrayList<String> username) {
        groups.get(gref).addMemberAndNotify(username);
    }
    
    public void removeGroupMember(String gref, String username){
        groups.get(gref).removeMember(username);
    }
    
    public void leaveGroup(String gref) {
        groups.get(gref).leaveGroup();
        groups.remove(gref);
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
        public GroupListener onNewGroupCreated(String gref, ArrayList<String> members, String groupName);
        public void onMemberConnected(String username);
        public void onMemberDisconnected(String username);
        public void onServerDown();
        public void onServerUp();
    }
    

   
}
