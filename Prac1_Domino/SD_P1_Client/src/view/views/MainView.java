/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import view.framework.View;
import java.util.Scanner;
import view.framework.ViewController;
import view.menu.Menu;

/**
 *
 * @author zenbook
 */
public class MainView extends View{

    private static enum OpcionsMenuPrincipal {
        PLAY, CHANGE_NAME, CHANGE_IP_PORT, SEE_STATS, SORTIR
    };
    
    private static final String[] descMenuPrincipal = {
        "Play a match",
        "Change your user name",
        "Change ip and port",
        "Stats",
        "Exit application"
    };

    public MainView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "You're welcome "+parent.getController().getUserName()+ "!";
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
                case CHANGE_IP_PORT:
                    parent.saveView();
                    return ChangeIPPort.class;
                case SEE_STATS:
                    parent.saveView();
                    return StatsView.class;
                case SORTIR:
                    System.out.println("Bye bye!");
                    break;
            }
        } while (op != OpcionsMenuPrincipal.SORTIR);
        return null;
    }
    
}
