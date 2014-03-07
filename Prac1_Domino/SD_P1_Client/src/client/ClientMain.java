package client;

/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */



import view.framework.ViewController;

/**
 *
 * @author Xavi Moreno
 */
public class ClientMain {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        if(args.length != 2) {
            System.out.println("You must indicate -ip <ip>:<port>");
            return;
        }
        String[] ipport = args[1].split(":");
        if(ipport.length != 2) {
            System.out.println("You must indicate -ip <ip>:<port>");
            return;
        }
        String ip = ipport[0];
        int port;
        try{
            port = Integer.parseInt(ipport[1]);
        }catch(NumberFormatException ex) {
            System.out.println("Port must be a integer number");
            return;
        }

        /*Donem control al ViewController*/
        ViewController viewController = new ViewController(ip, port);
        viewController.exec();
    }
    
}
