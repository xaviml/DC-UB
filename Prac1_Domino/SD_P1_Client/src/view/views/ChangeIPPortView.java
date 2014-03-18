/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import view.framework.View;
import controller.Controller;
import java.util.Scanner;
import view.framework.ViewController;

/**
 * This view allows to change the ip and port.
 * 
 * @author Xavi Moreno
 */
public class ChangeIPPortView extends View{

    public ChangeIPPortView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "Change ip and port here";
    }

    @Override
    public Class run(Scanner sc) {
        Controller c = parent.getController();
        System.out.println("Old IP: "+c.getIp());
        System.out.print("New IP: ");
        
        String ip = sc.nextLine();
        
        System.out.println("Old port: "+c.getPort());
        boolean flag;
        int port = c.getPort();
        do{
            System.out.print("New port: ");
            try {
                String input = sc.nextLine();
                port = Integer.parseInt(input);
                flag = port <= 65535 && port >= 0;
            }catch(NumberFormatException ex) {
                flag = false;
            }
        }while(!flag);
       
        
        c.setIp(ip);
        c.setPort(port);
        return null;
    }

}
