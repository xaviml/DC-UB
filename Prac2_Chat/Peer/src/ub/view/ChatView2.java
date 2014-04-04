/*************************************************************************
* Compilation: javac GuiChat.java
* Execution: java GuiChat name host
*
*************************************************************************/
 
package ub.view;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
 
public class ChatView2 extends JFrame implements ActionListener {
 
    private String screenName;

    // GUI stuff
    private JTextArea enteredText = new JTextArea(10, 32);
    private JTextField typedText = new JTextField(32);
    
    
    private JTabbedPane jTabs;
    private JList users;
 
    public ChatView2(String screenName, String hostName) {
        
        
        users = new JList(new DefaultListModel());
        addUser("Xavi");
        addUser("ASDFAISDFIASDFH");
        users.setSize(new Dimension(100, 100));
        
        DefaultListCellRenderer renderer =  (DefaultListCellRenderer)users.getCellRenderer();  
        renderer.setHorizontalAlignment(JLabel.CENTER); 
        

    // close output stream - this will cause listen() to stop and exit
        this.screenName = screenName;
        addWindowListener(
        new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            //onclose...
            }
        });


        // create GUI stuff
        enteredText.setEditable(false);
        enteredText.setBackground(Color.LIGHT_GRAY);
        typedText.addActionListener(this);

        Container content = getContentPane();
        content.add(new JScrollPane(enteredText), BorderLayout.CENTER);
        content.add(typedText, BorderLayout.SOUTH);
        content.add(users, BorderLayout.WEST);


        // display the window, with focus on typing box
        setTitle("GuiChat 1.0: [" + screenName + "]");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        typedText.requestFocusInWindow();
        setVisible(true);

    }   
    
    private void createNewTab(String title, JComponent content) {
        
    }
    
    private void addUser(String user) {
        DefaultListModel model = (DefaultListModel) users.getModel();
        model.addElement(user);
        
    }
 
    // process TextField after user hits Enter
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = "[" + screenName + "]: " + typedText.getText();
        //System.out.println(s);
        enteredText.insert(s + "\n", enteredText.getText().length());
        enteredText.setCaretPosition(enteredText.getText().length());
        typedText.setText("");
        typedText.requestFocusInWindow();
        //Talk with the other pal.
    }

    public static void main(String[] args) {
        ChatView2 client;
        /*if (args.length!= 2)
            System.out.println("java GuiChat <nickname> <host>");
        else
            client = new ChatView2(args[0], args[1]);*/
        client = new ChatView2("xaviml", null);
        
        //ChatController c = new ChatController("localhost", "xaviml", null, null);
    }
}
