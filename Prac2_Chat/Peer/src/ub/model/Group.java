/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import java.util.ArrayList;
import ub.common.GroupReference;
import ub.common.IPeer;
import ub.common.Message;

/**
 *
 * @author Pablo
 */
public class Group {
    private GroupListener guiListener;
    private ChatModelServices services;
    private ArrayList<String> members;
    private ArrayList<Message> messages;
    private GroupReference reference;
    private String name;
    
    public Group(ChatModelServices modelListener, GroupListener guiListener, ArrayList<String> members, String name, GroupReference ref){
        // If this is a new group, create a reference.
        if (ref == null) ref = createRef();
        
        this.messages = new ArrayList<>();
        this.reference = ref;
        this.members = members;
        this.services = modelListener;
        this.guiListener = guiListener;
        this.name = name;
        
    }

    private void leaveGroup(String user){
        synchronized(members){
            members.remove(user);
        }
        
    }
    
    private GroupReference createRef() {
        return new GroupReference();
    }
    
    public GroupReference getRef(){
        return reference;
    }
    
    public void writeMessage(Message m){
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
        this.messages.add(m);
        guiListener.onNewGroupMessageRecieved(reference, m);
    }
    
    public void reciveMessage(Message m){
        this.messages.add(m);
        guiListener.onNewGroupMessageRecieved(reference, m);
    }
    

    void setName(String newName) {
        this.name = newName;
        guiListener.onGroupNameChanged(reference, newName);
    }
    
    public interface GroupListener{
        public void onNewGroupMessageRecieved(GroupReference ref, Message m);
        public void onGroupNameChanged(GroupReference ref, String newName);
        public void onMemberLeaveGroup(String username);
    }
}
