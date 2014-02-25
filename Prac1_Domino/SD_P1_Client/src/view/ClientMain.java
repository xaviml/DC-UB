/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view;

import view.menu.Menu;
import controller.GameController;
import java.util.Scanner;

/**
 *
 * @author Xavi Moreno
 */
public class ClientMain {

    
    
    private GameController controller;
    
    /**
     * Constructor of Main class
     */
    public ClientMain() {
        controller = new GameController();
    }
    
    public void run() {
        System.out.println("Benvolgut "+this.controller.getUserName()+ "!");
        
        Scanner sc = new Scanner(System.in);
        mainMenu(sc);
    }
    
    public void mainMenu(Scanner sc) {
        
    }
    
    
    
    
    
    private void playGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void changeName() {
        
    }

    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /*ClientMain c = new ClientMain();
        c.run();*/
        
        ViewController viewController = new ViewController();
        viewController.run();
    }
    
}
