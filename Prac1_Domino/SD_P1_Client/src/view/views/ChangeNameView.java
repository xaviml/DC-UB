/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import view.framework.View;
import controller.Controller;
import controller.GameController;
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
        return "Canvia el teu nom aquí";
    }

    @Override
    public Class run(Scanner sc) {
        Controller c = parent.getController();
        System.out.println("Nom antic: "+c.getUserName());
        System.out.print("Escriu el nou nom d'usuari: ");
        String s = sc.next();
        c.setUsername(s);
        return null;
    }

}
