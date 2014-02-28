/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import controller.GameController;
import java.util.Scanner;
import view.ViewController;

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
        GameController c = parent.getGameController();
        System.out.println("Nom antic: "+c.getUserName());
        System.out.print("Escriu el nou nom d'usuari: ");
        String s = sc.next();
        c.setUsername(s);
        return null;
    }

}