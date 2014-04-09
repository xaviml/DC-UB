/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.beans.PropertyEditorManager;
import java.util.HashMap;
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

/**
 *
 * @author Xavi Moreno
 */
public class MessageBox extends JPanel implements Chat.ChatListener{

    private String me; //Color: CYAN
    private HashMap<String, Color> colors;
    private String[] others;
    
    private String nameChat;
    private String lastUser;
    private boolean isEmpty;
    private boolean isGroup;
    
    private OnMessageBoxListener listener;
    
    private JTextPane pane;
    
    public MessageBox(String name, String me, String[] others, boolean group,OnMessageBoxListener listener) {
        this.me = me;
        this.lastUser = "";
        this.nameChat = name;
        this.others = others;
        this.colors = new HashMap<>();
        this.isEmpty = true;
        this.isGroup = group;
        
        this.listener = listener;
        
        Color[] c = {Color.RED,  Color.ORANGE, Color.GREEN, Color.PINK, Color.DARK_GRAY};
        for (int i = 0; i < others.length; i++) {
            this.colors.put(others[i], c[i%c.length]);
        }
        
        setLayout(new GridLayout(0, 1));
        setBorder(new LineBorder(Color.WHITE, 1, true));
        
        pane = new JTextPane();
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
        this.isEmpty = false;
    }
    
    public synchronized void writeMessageOther(String user, String msg) {
        if(!lastUser.equals(user)) {
            String space = isEmpty ? "":"\n";
            addMessage(space+user+"\n", colors.get(user), true);
        }
        addMessage(msg+"\n", Color.BLACK, false);
        this.isEmpty = false;
        lastUser = user;
        
        if(!isGroup)
            listener.newMessageChat(getFirstUser());
    }
    
    private void addMessage(String msg, Color c, boolean bold) {
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
        addMessage("\n"+user, this.colors.get(user), false);
        addMessage(" is disconnected.", Color.gray, false);
        this.lastUser = "";
    }
    
    public synchronized void writeConnectUser() {
        String user = getFirstUser();
        addMessage("\n"+user, this.colors.get(user), false);
        addMessage(" is connected.", Color.gray, false);
        this.lastUser = "";
    }
    
    public boolean isGroup() {
        return this.isGroup;
    }

    public String getFirstUser() {
        return this.others[0];
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
    
    
    
    /**
     * Interfaz para comunicar-me con la view principal
     * 
     */
    
    public interface OnMessageBoxListener {
        public void userIsTyping(MessageBox m);
        public void newMessageChat(String user);
    }
}
