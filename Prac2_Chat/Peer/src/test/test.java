/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.InvalidUserNameException;
import ub.common.Message;
import ub.controller.ChatController;
import ub.model.Chat;
import ub.model.ChatModel.ChatRoomListener;

/**
 *
 * @author kirtash
 */
public class test implements ChatRoomListener{
    
    public test(){
        run();
    }
    
    public void run(){
        ChatController c = new ChatController(this);
        try {
            c.register("127.0.0.1", 8080, "User1");
            c.writeMessage("User3", "You are so gay");
            c.writeMessage("User2", "Yeah, so you are");
        } catch (RemoteException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidUserNameException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[]args){
        new test();
    }

    @Override
    public Chat.ChatListener onNewChatCreated(String username) {
        System.err.println("New Chat Created with "+username);
        return new Chat.ChatListener() {

            @Override
            public void onNewMessageRecived(Message m) {
                System.err.println(m.getMessage());
            }
        };
    }

    @Override
    public void onMemberConnected(String username) {
        System.err.println("New user Connected "+username);
    }

    @Override
    public void onMemberDisconnected(String username) {
        System.err.println("User Disconected");
    }
}
