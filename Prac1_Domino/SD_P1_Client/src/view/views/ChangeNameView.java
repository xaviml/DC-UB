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
 * This class allows to change the username of player.
 * 
 * @author Xavi Moreno
 */
public class ChangeNameView extends View{

    public ChangeNameView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "Change your user name here";
    }

    @Override
    public Class run(Scanner sc) {
        Controller c = parent.getController();
        System.out.println("Old user name: "+c.getUserName());
        System.out.print("New user name: ");
        String s = sc.next();
        c.setUsername(s);
        return null;
    }

}
