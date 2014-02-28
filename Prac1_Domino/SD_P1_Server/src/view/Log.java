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
    public static enum MessageType{MONITORING, ERROR, GAME, CONNECTION}
    private OnLogActionListener listener;
    private ArrayList<String> logBuffer;
    private ArrayList<String> newConnection;
    private ArrayList<String> disconnected;
    private ArrayList<String> gameCreated;
    private ArrayList<String> gameDestroyed;
    
    private boolean connectionLogs;
    private boolean errorLogs;
    private boolean gameLogs;
    
    
    public Log(){
        this.logBuffer = new ArrayList<>();
        this.connectionLogs = true;
        this.errorLogs = true;
        this.gameLogs = true;
    }
    
    public void setActionListener(OnLogActionListener listener){
        this.listener = listener;
    }
    // TOGGLING MESSAGES
    public void toggleConnections(){
        connectionLogs = !connectionLogs;
    }
    public void toggleErrors(){
        errorLogs = !errorLogs;
    }
    
    public void toggleGames(){
        gameLogs = !gameLogs;
    }
    
    
    //WRITING METHODS
    public synchronized void writePlain(String s){
        listener.onAddLog(s);
    }
    public synchronized void write(String _class, String message, MessageType type){
        if ((type == MessageType.ERROR && !errorLogs) || (type == MessageType.CONNECTION && !connectionLogs) || (type == MessageType.GAME && !gameLogs)){
            return;
        }
        String s = "["+type.name()+"]";
        listener.onAddLog(s+" -- ["+_class+"] :: "+message);
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
