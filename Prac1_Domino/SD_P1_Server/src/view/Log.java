/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import java.util.ArrayList;

/**
 * This will be the buffer that contain logs. Any class that want to place a log
 * in the GUI text pane must throw it to this Class. GUI will notify all changes.
 * @author Pablo
 */
public class Log{
    private OnLogActionListener listener;
    private ArrayList<String> buffer;
    
    public Log(){
        this.buffer = new ArrayList<>();
    }
    
    public void setActionListener(OnLogActionListener listener){
        this.listener = listener;
    }
    public synchronized void write(String s){
        this.buffer.add(s);
        listener.actionPerformed();
        
    }
    
    public synchronized String readLog(){
        return buffer.remove(0);
    }
    
    public interface OnLogActionListener{
        public void actionPerformed();
    }
}
