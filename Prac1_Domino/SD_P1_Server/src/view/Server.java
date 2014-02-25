/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import controller.ServerController;

/**
 *
 * @author Pablo
 */
public class Server {
    private Log log;
    private ServerMainWindow GUI;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Server s = new Server();
    }
    
    
    public Server(){
        this.log = new Log();
        this.GUI = new ServerMainWindow(this.log);
        
        writeWelcome();
        this.GUI.initServer();
        
    }
    
    public void writeWelcome(){
        this.log.write("|==========================================|");
        this.log.write("|==========SOFTWARE DISTRIBUIT=============|");
        this.log.write("|==========================================|");
        this.log.write("|Software developed by Xavi Moreno and");
        this.log.write("|Pablo Martinez");
        this.log.write("|==========================================|");
    }
    
    
}
