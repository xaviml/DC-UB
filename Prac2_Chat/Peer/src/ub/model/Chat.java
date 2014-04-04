/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.model;

import ub.common.Message;
import java.util.ArrayList;
import ub.common.IPeer;

/**
 *
 * @author Pablo
 */
public class Chat{
    private ChatListener listener;
    private ArrayList<Message> messages;
    private IPeer member;
    
    
    public Chat(ChatListener listener, IPeer peer){
        // Call super constructor
        this.messages = new ArrayList<>();
        this.listener = listener;
        this.member = peer;
    }
    
    protected void writeMessage(Message m) {
        this.messages.add(m);
        listener.onNewMessageRecived(m); // Notify a new message
    }

    public interface ChatListener{
        public void onNewMessageRecived(Message m);
    }
}
