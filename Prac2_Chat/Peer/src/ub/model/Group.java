/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import ub.common.GroupReference;
import ub.common.IPeer;
import ub.common.Message;
import ub.model.workers.NotifyGroup;

/**
 *
 * @author Pablo
 */
public class Group {
    private ExecutorService executor;
    private final GroupListener guiListener;
    private final ChatModelServices services;
    private final ArrayList<String> members;
    private final ArrayList<Message> messages;
    private final GroupReference reference;
    private String name;
    
    public Group(ChatModelServices modelListener, GroupListener guiListener, ArrayList<String> members, String name, GroupReference ref){
        // If this is a new group, create a reference.
        this.messages = new ArrayList<>();
        this.reference = ref;
        this.members = members;
        this.services = modelListener;
        this.guiListener = guiListener;
        this.name = name;
        
    }
    
    public GroupReference getRef(){
        return reference;
    }
    
    public void writeMessage(Message m){
        this.executor = Executors.newFixedThreadPool(10);
        for (String s:members) {
            if (s.equals(services.getMyUserName()))continue;
            IPeer p = services.getIPeerByName(s);
            if (p == null){
                // This might never happens...
                removeMember(name);
                continue;
            }
            executor.execute(new NotifyGroup(services, reference, m, p, s));
        }
        executor.shutdown();
        
        /*
        for (String s: members) {
            if (s.equals(services.getMyUserName()))continue;
            IPeer p = services.getIPeerByName(name);
            if (p == null){
                members.remove(s);
                continue;
            }
            try{
                p.writeMessage(reference, m);
            }catch(RemoteException rem){
                services.notifyDisconnectedClient(name);
            }
        }
        */
        this.messages.add(m);
        guiListener.onNewGroupMessageRecieved(m);
    }
    
    public void reciveMessage(Message m){
        this.messages.add(m);
        guiListener.onNewGroupMessageRecieved(m);
    }
    

    void setName(String newName) {
        this.name = newName;
        guiListener.onGroupNameChanged(newName);
    }

    void addMember(String username) {
        synchronized(members){
            members.add(username);
        }
    }
    
    void removeMember(String username){
        synchronized(members){
            members.remove(username);
        }
        guiListener.onMemberLeaveGroup(username);
    }
    
    public interface GroupListener{
        public void onNewGroupMessageRecieved(Message m);
        public void onGroupNameChanged(String newName);
        public void onMemberLeaveGroup(String username);
    }
}
