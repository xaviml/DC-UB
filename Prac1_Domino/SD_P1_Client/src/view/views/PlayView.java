/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import java.util.Scanner;
import view.ViewController;
import view.menu.Menu;

/**
 *
 * @author zenbook
 */
public class PlayView extends View{

    private static enum OpcionsPlayMenu {
        SEE_BOARD, SEE_HAND, STEAL, SORTIR
    };
    
    private static final String[] descPlayMenu = {
        "Veure taulell",
        "Veure la mà",
        "Robar fitxa",
        "Sortir"
    };
    
    public PlayView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "Play";
    }

    @Override
    public Class run(Scanner sc) {
        Menu<OpcionsPlayMenu> menu;
        menu = new Menu(OpcionsPlayMenu.values(), descPlayMenu);
        OpcionsPlayMenu op;

        do {
            menu.mostrarMenu();
            op = menu.getOpcio(sc);
            
            switch (op) {
                
            }
        } while (op != OpcionsPlayMenu.SORTIR);
        return null;
    }
    
}
