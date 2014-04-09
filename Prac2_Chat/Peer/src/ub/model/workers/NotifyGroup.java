/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model.workers;

import java.rmi.RemoteException;
import java.util.ArrayList;
import ub.common.GroupReference;
import ub.common.IPeer;
import ub.common.Message;
import ub.model.ChatModelServices;

/**
 *
 * @author kirtash
 */
public class NotifyGroup implements Runnable{
    private enum Instruction{ADD_GROUP,SEND_MESSAGE,NEW_MEMBER};
    private Instruction instruction;
    private IPeer adreesse;
    private String username;
    private ArrayList<String> members;
    private Message message;
    private String groupName;
    private String newMemberName;
    private GroupReference gref;
    private ChatModelServices services;

    public NotifyGroup(ChatModelServices services, GroupReference gref, String groupName, ArrayList<String> members, IPeer adreesse, String username){
        this.instruction = Instruction.ADD_GROUP;
        this.groupName = groupName;
        this.gref = gref;
        this.members = members;
        this.username = username;
        this.adreesse = adreesse;
        this.services = services;

    }
    
    public NotifyGroup(ChatModelServices services, String groupName, GroupReference gref, String newMemberName, IPeer adreesse, String username){
        this.instruction = Instruction.NEW_MEMBER;
        this.gref = gref;
        this.newMemberName = newMemberName;
        this.username = username;
        this.adreesse = adreesse;
        this.services = services;

    }
    
    
    public NotifyGroup(ChatModelServices services, GroupReference gref, Message m, IPeer adreesse, String username){
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
            }
        } catch (RemoteException ex) {
            services.notifyDisconnectedClient(username);
        }
        
    }
    
}
