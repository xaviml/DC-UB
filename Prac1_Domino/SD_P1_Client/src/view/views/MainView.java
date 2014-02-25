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
public class MainView extends View{

    private static enum OpcionsMenuPrincipal {
        PLAY, CHANGE_NAME, SORTIR
    };
    
    private static final String[] descMenuPrincipal = {
        "Jugar una partida",
        "Canviar el meu nom d'usuari",
        "Sortir"
    };

    public MainView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "Benvolgut "+parent.getGameController().getUserName()+ "!";
    }

    @Override
    public Class run(Scanner sc) {
        Menu<OpcionsMenuPrincipal> menu;
        menu = new Menu(OpcionsMenuPrincipal.values(), descMenuPrincipal);
        OpcionsMenuPrincipal op;

        do {
            menu.mostrarMenu();
            op = menu.getOpcio(sc);
            
            switch (op) {
                case PLAY:
                    parent.saveView();
                    return PlayView.class;
                case CHANGE_NAME:
                    parent.saveView();
                    return ChangeNameView.class;
                case SORTIR:
                    System.out.println("Fins un altre!");
                    break;
            }
        } while (op != OpcionsMenuPrincipal.SORTIR);
        return null;
    }
    
}
