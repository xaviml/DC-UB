/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model.workers;

import java.rmi.RemoteException;
import java.util.ArrayList;
import ub.common.IPeer;
import ub.common.Message;
import ub.model.ChatModelServices;

/**
 * This is a Worker that notify the events going on in a group. The variable
 * instruction, contains the current task to be executed.
 * @author kirtash
 */
public class NotifyGroup implements Runnable{
    private enum Instruction{ADD_GROUP,SEND_MESSAGE,NEW_MEMBER,LEAVE_GROUP};
    private final Instruction instruction;
    private final IPeer adreesse;
    private final String username;
    private ArrayList<String> members;
    private Message message;
    private String groupName;
    private String newMemberName;
    private final String gref;
    private final ChatModelServices services;

    public NotifyGroup(ChatModelServices services, String gref, String groupName, ArrayList<String> members, IPeer adreesse, String username){
        this.instruction = Instruction.ADD_GROUP;
        this.groupName = groupName;
        this.gref = gref;
        this.members = members;
        this.username = username;
        this.adreesse = adreesse;
        this.services = services;

    }
    
    public NotifyGroup(ChatModelServices services, String gref, String newMemberName, IPeer adreesse, String username, boolean newMember){
        this.instruction = (newMember)?Instruction.NEW_MEMBER: Instruction.LEAVE_GROUP;
        this.gref = gref;
        this.newMemberName = newMemberName;
        this.username = username;
        this.adreesse = adreesse;
        this.services = services;

    }
    
    
    public NotifyGroup(ChatModelServices services, String gref, Message m, IPeer adreesse, String username){
        this.instruction = Instruction.SEND_MESSAGE;
        this.gref = gref;
        this.message = m;
        this.username = username;
        this.adreesse = adreesse;
        this.services = services;
    }
    @Override
    public void run() {
        try {
            switch(instruction){
                case ADD_GROUP:
                    adreesse.addGroup(gref, groupName, members);
                    break;
                case NEW_MEMBER:
                    adreesse.userJoinedGroup(gref, newMemberName);
                    break;
                case SEND_MESSAGE:
                    adreesse.writeMessage(gref, message);
                    break;
                case LEAVE_GROUP:
                    adreesse.leaveGroup(gref, newMemberName);
                    break;
            }
        } catch (RemoteException ex) {
            services.notifyDisconnectedClient(username);
        }
        
    }
    
}
