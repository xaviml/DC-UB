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
    private ArrayList<String> logBuffer;
    private ArrayList<String> newConnection;
    private ArrayList<String> disconnected;
    private ArrayList<String> gameCreated;
    private ArrayList<String> gameDestroyed;
    
    public Log(){
        this.logBuffer = new ArrayList<>();
    }
    
    public void setActionListener(OnLogActionListener listener){
        this.listener = listener;
    }
    public synchronized void write(String s){
        listener.onAddLog(s);
    }
    public synchronized void addConnection(String s){
        listener.onNewConnection(s);
    }
    public synchronized void removeConnection(String s){
        listener.onDisconnect(s);
    }
    
    public synchronized String readLog(){
        return logBuffer.remove(0);
    }
    
    public interface OnLogActionListener{
        public void onAddLog(String s);
        public void onGameCreated(String s);
        public void onGameDestroyed(String s);
        public void onNewConnection(String s);
        public void onDisconnect(String s);
        
    }
}
