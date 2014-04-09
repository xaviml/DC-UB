/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ub.common.GroupReference;
import ub.common.UserInUseException;
import ub.controller.ChatController;
import ub.exceptions.WrongAdresseeException;
import ub.model.Chat;
import ub.model.ChatModel;
import ub.model.Group;

/**
 *
 * @author zenbook
 */
public class ChatView extends JFrame implements ChatModel.ChatRoomListener, MessageBox.OnMessageBoxListener{

    private ConcurrentHashMap<String, MessageBox> chats; //For individual chats
    
    private ArrayList<GroupObject> orderedListGroups;
    private ConcurrentHashMap<GroupReference, MessageBox> hashGroup;
    
    private String username;
    
    private ChatController controller;
    private boolean isTyping;
    
    private Timer timer;
    
    /**
     * Creates new form ChatView
     */
    public ChatView() {
        initComponents();
        
        controller = new ChatController(this);
        
        chats = new ConcurrentHashMap<>();
        isTyping = false;
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((screen.getWidth() - getWidth()) /2);
        int y = (int) ((screen.getHeight() -getHeight()) /2);
        setLocation(x, y);
        
        lbl_typing.setVisible(false);
        btn_send.setVisible(false);
        tf_send.setVisible(false);
        tab_chats.setVisible(false);
        
        list_users.setModel(new DefaultListModel());
        list_groups.setModel(new DefaultListModel());
        
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)list_groups.getCellRenderer();  
        renderer.setHorizontalAlignment(JLabel.CENTER); 
        renderer = (DefaultListCellRenderer)list_users.getCellRenderer();  
        renderer.setHorizontalAlignment(JLabel.CENTER); 
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.disconnect();
            }
        });
        
        tab_chats.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if(tab_chats.getTabCount()<2) return;
                MessageBox m = getCurrentMessageBox();
                ButtonTabComponent b = getButtonTabComponent(m);
                b.visibleStar(false);
            }
        });
    }
    
    public void registry(String IP, int port, String user) throws RemoteException, NotBoundException, MalformedURLException, UserInUseException {
        this.username = user;
        controller.register(IP, port, user);
        setTitle("Chat RMI["+user+"]");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tab_chats = new javax.swing.JTabbedPane();
        btn_send = new javax.swing.JButton();
        tf_send = new javax.swing.JTextField();
        tab_users = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_users = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_groups = new javax.swing.JList();
        lbl_typing = new javax.swing.JLabel();
        btn_createGroup = new javax.swing.JButton();
        btn_leftGroup = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat RMI");
        setMinimumSize(new java.awt.Dimension(355, 235));

        tab_chats.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        tab_chats.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        tf_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tf_sendActionPerformed(evt);
            }
        });
        tf_send.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tf_sendKeyPressed(evt);
            }
        });

        tab_users.setBorder(null);
        tab_users.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        tab_users.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tab_usersStateChanged(evt);
            }
        });

        list_users.setBorder(null);
        list_users.setFont(new java.awt.Font("DejaVu Sans Mono", 2, 15)); // NOI18N
        list_users.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_usersMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(list_users);

        tab_users.addTab("Users", jScrollPane1);

        list_groups.setBorder(null);
        list_groups.setFont(new java.awt.Font("DejaVu Sans Mono", 2, 15)); // NOI18N
        list_groups.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        list_groups.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_groupsMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(list_groups);

        tab_users.addTab("Groups", jScrollPane2);

        lbl_typing.setText("typing...");

        btn_createGroup.setText("Create group");
        btn_createGroup.setToolTipText("You must select users to create a group");
        btn_createGroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_createGroupActionPerformed(evt);
            }
        });

        btn_leftGroup.setText("Left group");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_createGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_leftGroup, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(tab_users, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 464, Short.MAX_VALUE)
                        .addComponent(lbl_typing))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tab_chats, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tf_send)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tab_chats, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_typing, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_send)
                            .addComponent(tf_send, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_createGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_leftGroup, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tab_users)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void list_groupsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_groupsMousePressed
        if(evt.getClickCount() == 2) {
            
        }
    }//GEN-LAST:event_list_groupsMousePressed

    private void list_usersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_usersMousePressed
        int selected = list_users.getSelectedIndex();
        if(selected == -1) return;
        DefaultListModel model = (DefaultListModel) list_users.getModel();
        String name = (String) model.get(selected);
        if(evt.getClickCount() == 2) {
            MessageBox m = getMessageBoxChat(name);
            openTab(m, false, true);
            tf_send.requestFocusInWindow();
        }
    }//GEN-LAST:event_list_usersMousePressed

    private void tf_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tf_sendActionPerformed
        sendMessage();
    }//GEN-LAST:event_tf_sendActionPerformed

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        sendMessage();
    }//GEN-LAST:event_btn_sendActionPerformed

    private void tf_sendKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tf_sendKeyPressed
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) return;
        final MessageBox m = getCurrentMessageBox();
        if(m.isGroup()) return;
        if(!isTyping) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    controller.userIsTyping(m.getFirstUser());
                }
            }).start();
        }
        this.isTyping = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                    isTyping = false;
            }
        }, 1000);
        
    }//GEN-LAST:event_tf_sendKeyPressed

    private void tab_usersStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tab_usersStateChanged
        int idx = tab_users.getSelectedIndex();
        if(tab_users.getTitleAt(idx).equals("Users")) {
            btn_leftGroup.setVisible(false);
        }else{
            btn_leftGroup.setVisible(true);
        }
    }//GEN-LAST:event_tab_usersStateChanged

    private void btn_createGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_createGroupActionPerformed
        int[] idxs = list_users.getSelectedIndices();
    }//GEN-LAST:event_btn_createGroupActionPerformed

    private void sendMessage() {
        final MessageBox m = getCurrentMessageBox();
        
        final String msg = tf_send.getText().trim();
        if(msg.isEmpty()) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    controller.writeMessage(m.getFirstUser(), msg);
                }catch(WrongAdresseeException ex){
                    m.writeMessageMe(msg);
                }
            }
        }).start();
        tf_send.setText("");
    }
    
    private void openTab(MessageBox m, boolean group, boolean selectedTab) {
        if(tab_chats.getTabCount() == 0) {
            btn_send.setVisible(true);
            tf_send.setVisible(true);
            tab_chats.setVisible(true);
        }
        int idx = tab_chats.indexOfTab(m.getNameChat());
        if(idx == -1) { //if tab doesn't exist...
            tab_chats.addTab(m.getNameChat(), m);
            idx = tab_chats.getTabCount()-1;
            tab_chats.setTabComponentAt(idx, new ButtonTabComponent(tab_chats, new Runnable() {
                @Override
                public void run() {
                    lbl_typing.setVisible(false);
                    if(tab_chats.getTabCount() == 0) {
                        btn_send.setVisible(false);
                        tf_send.setVisible(false);
                        tab_chats.setVisible(false);
                    }
                }
            }));
        }
        
        //Select the tab
        if(selectedTab || tab_chats.getTabCount() == 1) {
            tab_chats.setSelectedIndex(idx);
        }else {
            ButtonTabComponent b = getButtonTabComponent(m);
            b.visibleStar(true);
        }
    }
    
    private void addUser(String user) {
        DefaultListModel model = (DefaultListModel) list_users.getModel();
        model.addElement(user);
    }
    
    private void addGroup(GroupObject group) {
        this.orderedListGroups.add(group);
        GroupListModel m = new GroupListModel(this.orderedListGroups);
        list_groups.setModel(m);
    }
    
    private void removeUser(String user) {
        DefaultListModel model = (DefaultListModel) list_users.getModel();
        model.removeElement(user);
    }
    
    private void removeGroup(GroupObject group) {
        this.orderedListGroups.remove(group);
        GroupListModel m = new GroupListModel(this.orderedListGroups);
        list_groups.setModel(m);
    }
    
    private MessageBox getMessageBoxChat(String username) {
        MessageBox m;
        if(chats.containsKey(username)) {
            m = chats.get(username);
        }else{
            m = new MessageBox(username, this.username, new String[]{username}, false, this);
            chats.put(username, m);
        }
        return m;
    }
    
    /*private MessageBox getMessageBoxGroup(GroupReference ref, String[] users, String name) {
        MessageBox m;
        if(hashGroup.containsKey(ref)) {
            m = hashGroup.get(ref);
        }else{
            m = new MessageBox(username, this.username, new String[]{username}, false, this);
            chats.put(username, m);
        }
        return m;
    }*/
    
    private ButtonTabComponent getButtonTabComponent(MessageBox m) {
        int idx = tab_chats.indexOfComponent(m);
        return (ButtonTabComponent) tab_chats.getTabComponentAt(idx);
    }
    
    private MessageBox getCurrentMessageBox() {
        return (MessageBox) tab_chats.getSelectedComponent();
    } 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_createGroup;
    private javax.swing.JButton btn_leftGroup;
    private javax.swing.JButton btn_send;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_typing;
    private javax.swing.JList list_groups;
    private javax.swing.JList list_users;
    private javax.swing.JTabbedPane tab_chats;
    private javax.swing.JTabbedPane tab_users;
    private javax.swing.JTextField tf_send;
    // End of variables declaration//GEN-END:variables

    @Override
    public Chat.ChatListener onNewChatCreated(String username) {
        MessageBox msgBox = getMessageBoxChat(username);
        openTab(msgBox, false, false);
        return msgBox;
    }

    @Override
    public void onMemberConnected(String username) {
        if(username.equals(this.username)) return;
        addUser(username);
        if(chats.containsKey(username)) {
            chats.get(username).writeConnectUser();
        }
    }

    @Override
    public void onMemberDisconnected(String username) {
        removeUser(username);
        getMessageBoxChat(username).writeErrorMessage();
    }

    @Override
    public Group.GroupListener onNewGroupCreated(GroupReference gref, ArrayList<String> members, String groupName) {
        GroupObject groupObj = new GroupObject(gref, groupName);
        addGroup(groupObj);
        String[] users = members.toArray(new String[members.size()]);
        MessageBox group = new MessageBox(groupName, username, users, true, this);
        hashGroup.put(gref, group);
        openTab(group, true, false);
        return group;
    }

    @Override
    public void userIsTyping(MessageBox m) {
        if(m == getCurrentMessageBox()) {
            final Object mutex = new Object();
            synchronized(mutex) {
                if(!lbl_typing.isVisible())
                    lbl_typing.setVisible(true);
            }
            if(timer != null)
                timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized(mutex) {
                        lbl_typing.setVisible(false);
                    }
                }
            }, 1500);
        }
    }

    @Override
    public void newMessageChat(String user) {
        if(lbl_typing.isVisible())
            lbl_typing.setVisible(false);
        MessageBox m = chats.get(user);
        if(m == getCurrentMessageBox()) return;
        openTab(m, false, false);
        
    }
    
    @Override
    public void onGroupNameChanged(String oldName, String newName) {
        
    }

    @Override
    public void onServerDown() {
        
    }

    @Override
    public void onServerUp() {
        
    }
}
