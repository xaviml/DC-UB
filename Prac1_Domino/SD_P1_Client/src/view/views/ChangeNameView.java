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
        System.out.print("Write here your new user name: ");
        String s = sc.next();
        c.setUsername(s);
        return null;
    }

}
