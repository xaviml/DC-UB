/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import ub.common.Message;
import java.util.ArrayList;
import ub.common.IPeer;

/**
 *
 * @author Pablo
 */
public class Chat{
    private ChatModelServices services;
    private ChatListener listener;
    private ArrayList<Message> messages;
    private ArrayList<String> members;
    
    
    public Chat(ChatModelServices serv, ChatListener listener, ArrayList<String> peer){
        // Call super constructor
        this.messages = new ArrayList<>();
        this.listener = listener;
        this.members = peer;
        this.services = serv;
    }
    
    protected void writeMessage(Message m) {
        for (String s: members) {
            IPeer p = services.getIPeerByName(s);
            try {
                p.writeMessage(m);
            } catch (RemoteException ex) {
                members.remove(s);
                services.notifyDisconnectedClient(s);
            }
        }
    }
    
    protected void reciveMessage(Message m){
        messages.add(m);
        listener.onNewMessageRecived(m);
        
    }

    public interface ChatListener{
        public void onNewMessageRecived(Message m);
    }
}
