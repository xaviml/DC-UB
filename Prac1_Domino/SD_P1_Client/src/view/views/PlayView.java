/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import controller.Controller;
import controller.DominoGame;
import controller.GameController;
import java.util.Scanner;
import view.ViewController;
import view.menu.Menu;

/**
 *
 * @author zenbook
 */
public class PlayView extends View{

    private DominoGame mGame;
    private GameController mController;
    
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
        mController = parent.getController().createGame();
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
                case SEE_BOARD:
                    break;
                case SEE_HAND:
                    break;
                case STEAL:
                    break;
                case SORTIR:
                    break;
            }
        } while (op != OpcionsPlayMenu.SORTIR);
        return null;
    }
    
}
