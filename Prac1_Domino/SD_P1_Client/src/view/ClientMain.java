/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view;

/**
 *
 * @author Xavi Moreno
 */
public class ClientMain {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /*Donem control al ViewController*/
        ViewController viewController = new ViewController(args);
        viewController.exec();
    }
    
}
