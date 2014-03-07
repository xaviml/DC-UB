/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import view.framework.View;
import controller.Controller;
import java.util.InputMismatchException;
import java.util.Scanner;
import view.framework.ViewController;

/**
 *
 * @author Xavi Moreno
 */
public class ChangeIPPort extends View{

    public ChangeIPPort(ViewController parent) {
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
        System.out.print("Write the new IP: ");
        String ip = sc.next();
        
        System.out.println("Old port: "+c.getPort());
        boolean flag;
        int port = c.getPort();
        do{
            System.out.print("Write the new port: ");
            try {
                port = sc.nextInt();
                flag = true;
            }catch(InputMismatchException ex) {
                flag = false;
            }
        }while(!flag);
       
        
        c.setIp(ip);
        c.setPort(port);
        return null;
    }

}
