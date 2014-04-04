/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view;

import javax.swing.JTextArea;

/**
 *
 * @author Xavi Moreno
 */
public class MessageBox extends JTextArea{

    private String me;
    private String other;
    
    public MessageBox(String me, String other) {
        this.me = me;
        this.other = other;
    }
    
    public void writeMessageMe(String msg) {
        setText("[ "+me+" ]: "+msg);
    }
    
    public void writeMessageOther(String msg) {
        setText("[ "+other+" ]: "+msg);
    }
}
