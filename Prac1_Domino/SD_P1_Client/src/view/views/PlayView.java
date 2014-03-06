/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import view.framework.View;
import controller.DominoGame;
import controller.connection.GameController;
import java.util.Scanner;
import view.framework.ViewController;
import view.menu.Menu;

/**
 *
 * @author zenbook
 */
public class PlayView extends View{

    private DominoGame mGame;
    private GameController mGameController;

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
        mGameController = parent.getController().createGame();
    }

    @Override
    public String getTitle() {
        return "Play";
    }

    @Override
    public Class run(Scanner sc) {
        if(mGameController == null) {
            System.out.println("No s'ha pogut establir una connexió amb el servidor");
            return null;
        }
        Menu<OpcionsPlayMenu> menu;
        menu = new Menu(OpcionsPlayMenu.values(), descPlayMenu);
        OpcionsPlayMenu op;

        do {
            menu.mostrarMenu();
            op = menu.getOpcio(sc);
            
            switch (op) {
                case SEE_BOARD:
                    seeBoard();
                    break;
                case SEE_HAND:
                    break;
                case STEAL:
                    break;
                case SORTIR:
                    this.mGameController.close();
                    break;
            }
        } while (op != OpcionsPlayMenu.SORTIR);
        return null;
    }
    
    private void seeBoard() {
        System.out.println(mGame.getBoardPieces());
    }
}
