/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

/**
 *
 * @author Pablo
 */
public class Log {
    public boolean changed;
    public String log;
    
    public synchronized void write(String s){
        this.log = this.log+"\n"+s;
        changed = true;
    }
    
    public synchronized String readLog(){
        changed = false;
        return log;
    }
}
