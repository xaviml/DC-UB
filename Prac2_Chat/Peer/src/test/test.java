/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package test;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.common.GroupReference;
import ub.common.InvalidUserNameException;
import ub.common.Message;
import ub.controller.ChatController;
import ub.exceptions.WrongAdreseeException;
import ub.model.Chat;
import ub.model.ChatModel.ChatRoomListener;
import ub.model.Group;

/**
 *
 * @author kirtash
 */
public class test implements ChatRoomListener {
    
    public test(){
        run();
    }
    
    public void run(){
        ChatController c = new ChatController(this);
        try {
            c.register("127.0.0.1", 8080, "User3");
            System.out.println("Registered");
            /*synchronized(this){
                this.wait(5000);
            }*/
            c.writeMessage("User2", "You are so gay");
            //c.writeMessage("User", "Yeah, so you are");
        } catch (RemoteException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidUserNameException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongAdreseeException ex) {
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

    @Override
    public Group.GroupListener onNewGroupCreated(GroupReference gref, ArrayList<String> members, String groupName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
