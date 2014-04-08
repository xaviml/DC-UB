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
    private GroupListener listener;
    private ArrayList<IPeer> members;
    private ArrayList<Message> messages;
    private GroupReference reference;
    private String name;
    
    public Group(GroupListener listener, ArrayList<IPeer> members, String name, GroupReference ref){
        // If this is a new group, create a reference.
        if (ref == null) ref = createRef();
        
        this.messages = new ArrayList<>();
        this.reference = ref;
        this.members = members;
        this.listener = listener;
        this.name = name;
        
    }

    private GroupReference createRef() {
        return new GroupReference();
    }
    
    public GroupReference getRef(){
        return reference;
    }
    
    public void writeMessage(Message m) throws RemoteException{
        for (IPeer p: members) {
            p.writeMessageGroup(reference, m);
        }
    }
    
    public void reciveMessage(Message m){
        this.messages.add(m);
        listener.onNewGroupMessageRecieved(reference, m);
    }
    

    void setName(String newName) {
        this.name = newName;
        listener.onGroupNameChanged(reference, newName);
    }
    
    public interface GroupListener{
        public void onNewGroupMessageRecieved(GroupReference ref, Message m);
        public void onGroupNameChanged(GroupReference ref, String newName);
    }
}
