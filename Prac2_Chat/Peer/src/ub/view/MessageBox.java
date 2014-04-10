/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import ub.common.Message;
import ub.model.Chat;
import ub.model.Group;

/**
 *
 * @author Xavi Moreno
 */
public class MessageBox extends JPanel implements Chat.ChatListener, Group.GroupListener{

    private String me; //Color: CYAN
    private ConcurrentHashMap<String, Color> chatters;
    
    private Color[] colors = {Color.RED,  Color.ORANGE, Color.GREEN, Color.PINK, Color.DARK_GRAY};
    
    private String other; //For individual chat
    private String nameChat;
    private String lastUser;
    private boolean isEmpty;
    private boolean isGroup;
    private String gref;
    
    private OnMessageBoxListener listener;
    
    private JTextPane pane;
    
    public MessageBox(String name, String me, String[] others, String gref, OnMessageBoxListener listener) {
        this.me = me;
        this.lastUser = "";
        this.nameChat = name;
        this.gref = gref;
        
        this.other = others[0];
        this.chatters = new ConcurrentHashMap<>();
        
        this.isEmpty = true;
        this.isGroup = gref != null;
        
        this.listener = listener;
        for (String u : others) {
            if (u.equals(me)) continue;
            addMember(u);
        }
        
        setLayout(new GridLayout(0, 1));
        setBorder(new LineBorder(Color.WHITE, 1, true));
        
        pane = new JTextPane();
        pane.setEditable(false);
        pane.setMargin(new Insets(5, 5, 5, 5));
        add(new JScrollPane(pane));
        
    }
    
    public synchronized void writeMessageMe(String msg) {
        if(!lastUser.equals(me)) {
            String space = isEmpty ? "":"\n";
            addMessage(space+this.me+"\n", Color.BLUE, true);
        }
        addMessage(msg+"\n", Color.BLACK, false);
        lastUser = me;
    }
    
    public synchronized void writeMessageOther(String user, String msg) {
        if(!lastUser.equals(user)) {
            String space = isEmpty ? "":"\n";
            addMessage(space+user+"\n", chatters.get(user), true);
        }
        addMessage(msg+"\n", Color.BLACK, false);
        lastUser = user;
        
        if(!isGroup)
            listener.newMessageChat(getFirstUser());
    }
    
    private void addMember(String user) {
        this.chatters.put(user, colors[chatters.size()%colors.length]);
    }
    
    private void removeMember(String user) {
        this.chatters.remove(user);
    }
    
    private void addMessage(String msg, Color c, boolean bold) {
        if(c == null) 
            c = Color.BLACK;
        this.isEmpty = false;
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "DejaVu Sans Mono");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT);
        aset = sc.addAttribute(aset, StyleConstants.Bold, bold);

        pane.setEditable(true);
        int len = pane.getDocument().getLength();
        pane.setCaretPosition(len);
        pane.setCharacterAttributes(aset, false);
        pane.replaceSelection(msg);
        pane.setEditable(false);
    }
    
    public synchronized void writeErrorMessage() {
        String user = getFirstUser();
        addMessage("\n"+user, this.chatters.get(user), false);
        addMessage(" is disconnected.", Color.gray, false);
        this.lastUser = "";
    }
    
    public synchronized void writeConnectUser() {
        String user = getFirstUser();
        addMessage("\n"+user, this.chatters.get(user), false);
        addMessage(" is connected.", Color.gray, false);
        this.lastUser = "";
    }
    
    public boolean isGroup() {
        return this.isGroup;
    }
    
    public String getGroupReference() {
        return this.gref;
    }

    public String getFirstUser() {
        return this.other;
    }
    
    public String getNameChat() {
        return nameChat;
    }
    
    @Override
    public void onNewMessageRecived(final Message m) {
        if(m.getUsername().equals(this.me))
            writeMessageMe(m.getMessage());
        else
            writeMessageOther(m.getUsername(), m.getMessage());
    }

    @Override
    public void onUserTyping() {
        listener.userIsTyping(this);
    }

    @Override
    public void onMemberLeaveGroup(String username) {
        synchronized(this) {
            addMessage("\n"+username, this.chatters.get(username), false);
            addMessage(" has left group.", Color.gray, false);
            this.lastUser = "";
        }
        removeMember(username);
        refreshMembers();
        
    }
    
    public synchronized void refreshMembers() {
        ArrayList<String> users = new ArrayList<>();
        for (String user : this.chatters.keySet()) {
            users.add(user);
        }
        listener.refreshMembers(users);
    }

    @Override
    public void onNewGroupMessageRecieved(Message m) {
        if(m.getUsername().equals(this.me))
            writeMessageMe(m.getMessage());
        else
            writeMessageOther(m.getUsername(), m.getMessage());
    }

    @Override
    public void onGroupNameChanged(String newName) {
        listener.onGroupNameChanged(this.nameChat, newName);
        this.nameChat = newName;
    }

    @Override
    public void onNewMemberConnected(String username) {
        addMember(username);
        synchronized(this) {
            addMessage("\n"+username, this.chatters.get(username), false);
            addMessage(" joined", Color.gray, false);
            this.lastUser = "";
        }
        refreshMembers();
    }

    
    
    
    /**
     * Interfaz para comunicar-me con la view principal
     * 
     */
    
    public interface OnMessageBoxListener {
        //For indivual chat
        public void userIsTyping(MessageBox m);
        public void newMessageChat(String user);
        
        //For group
        public void onGroupNameChanged(String oldName, String newName);
        public void refreshMembers(ArrayList<String> members);
        
    }
}
