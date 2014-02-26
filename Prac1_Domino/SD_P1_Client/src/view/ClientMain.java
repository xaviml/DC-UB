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
