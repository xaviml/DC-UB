/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import java.rmi.RemoteException;
import ub.common.Message;
import java.util.ArrayList;
import ub.common.IPeer;
import ub.exceptions.UserDisconnectedException;

/**
 *
 * @author Pablo
 */
public class Chat{
    private final ChatModelServices services;
    private final ChatListener listener;
    private final ArrayList<Message> messages;
    private final String member;
    
    
    public Chat(ChatModelServices serv, ChatListener listener, String peer){
        // Call super constructor
        this.messages = new ArrayList<>();
        this.listener = listener;
        this.member = peer;
        this.services = serv;
    }
    
    protected void writeMessage(Message m) {
        if (member == null) throw new UserDisconnectedException();
        IPeer p = services.getIPeerByName(member);
        if (p == null) return;

        try {
            p.writeMessage(m);
        } catch (RemoteException ex) {
            services.notifyDisconnectedClient(member);
        }
        
        synchronized(messages){
            messages.add(m);
        }
        listener.onNewMessageRecived(m);
    }
    
    protected void reciveMessage(Message m){
        synchronized (messages){
            messages.add(m);
        }
        listener.onNewMessageRecived(m);
        
    }
    
    public void userIsTypingReceiver() {
        listener.onUserTyping();
    }
    
    public void userIsTypingSender(String username) {
        IPeer p = services.getIPeerByName(member);
        if (p == null) return;
        try {
            p.userIsTyping(username);
        } catch (RemoteException ex) {
            services.notifyDisconnectedClient(member);
        }
    }

    public interface ChatListener{
        public void onNewMessageRecived(Message m);
        public void onUserTyping();
    }
}
