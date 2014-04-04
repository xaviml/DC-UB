/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package deprecated;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.IPeer;
import deprecated.Chat.ChatListener;

/**
 *
 * @author Pablo
 */
public class ChatModel {
    public HashMap<IPeer,String> users;
    public ChatRoomListener chatRoomListener;
    public IdentityHashMap<Long,Chat> chats;
    private final Object KEY = new Object();
    
    public boolean wirteMessage(long idChat, Message m){
        if (!chats.containsKey(idChat)) return false;
        return true;
    }
    
    public void recieveMessage(IPeer peer, long idChat, Message m){
        Chat c = chats.get(idChat);
        if (c==null){
            //Create the chat.
            
            chatRoomListener.onNewChatListener(idChat, m.mess);
            c = new Chat(idChat);
            try {
                c.writeMessage(m);
            } catch (RemoteException ex) {
                Logger.getLogger(ChatModel.class.getName()).log(Level.SEVERE, null, ex);
            }
            chats.put(idChat, c);
        }
    }

    public void setListener(long idChat, ChatListener listener){
        chats.get(idChat).setListener(listener);
    }
    
    
    public void createChat(ArrayList<String> peers){ // When you want to create a chat.
        
    }
    public void joinChat(long idChat, ArrayList<IPeer> peers){ // When you join a group.
        
    }
    
    public void leaveChat(long idChat){
        
    }
    
    
    private long createIdChat(){
        long lo;
        synchronized(KEY){
            lo = (System.currentTimeMillis()*1000)+(System.nanoTime()%1000); // TimeStamp in nanoseconds   
        }
        return 3;
    }
    
    public void userConnected(String userName, IPeer peer){
        users.put(peer, userName);
        chatRoomListener.onClientConnectedListener(userName);
    }
    
    public interface ChatRoomListener{
        public void onNewChatListener(long chatId, String message);
        public void onClientDisconnectedCallbackListener(String peerName);
        public void onClientConnectedListener(String peerName);
    }
}
