/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private final String reference;
    private String name;
    
    public Group(ChatModelServices modelListener, GroupListener guiListener, ArrayList<String> members, String name, String ref){
        // If this is a new group, create a reference.
        this.messages = new ArrayList<>();
        this.reference = ref;
        this.members = members;
        this.services = modelListener;
        this.guiListener = guiListener;
        this.name = name;
        
    }
    
    public String getRef(){
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
    
    void addMemberAndNotify(ArrayList<String> newMembers) {
        // Check if function is being called with null parameter
        if (newMembers == null) return; // Avoid errors
        
        ArrayList<String> tmp = (ArrayList<String>)members.clone();
        
        // Remove duplications
        for(String s: members){
            if (newMembers.contains(s)) newMembers.remove(s);
        }

        System.out.println(members);
        members.addAll(newMembers);
        IPeer p;
        
        // Crete the executor
        this.executor = Executors.newFixedThreadPool(10);
        synchronized(members){
            for(String s: newMembers){
                // Add a new guy to the group
                p = services.getIPeerByName(s);
                try {
                    p.addGroup(reference, name, tmp);
                } catch (RemoteException ex) {
                    services.notifyDisconnectedClient(s);
                }
                
                if (tmp.contains(s)) continue;
                guiListener.onNewMemberConnected(s);
                for (String m: tmp){
                    p = services.getIPeerByName(m);
                    if (p== null) continue;
                    executor.execute(new Thread(new NotifyGroup(services, reference, s, p, m, true)));
                }
                

            }
            executor.shutdown();
            
        }
    }
    
    void addMember(String username) {
        synchronized(members){
            if (!members.contains(username)){
                members.add(username);
                guiListener.onNewMemberConnected(username);
            }
        }
    }
    public void leaveGroup(){
        this.executor = Executors.newFixedThreadPool(10);
        for (String s:members) {
            if (s.equals(services.getMyUserName()))continue;
            IPeer p = services.getIPeerByName(s);
            if (p == null){
                // This might never happens...
                removeMember(name);
                continue;
            }
            executor.execute(new NotifyGroup(services, reference, services.getMyUserName(), p, s, false));
        }
        executor.shutdown();   
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
        public void onNewMemberConnected(String username);
    }
}
