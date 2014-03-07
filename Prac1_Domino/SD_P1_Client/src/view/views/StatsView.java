/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import controller.Controller;
import view.framework.View;
import view.framework.Bundle;
import java.util.Scanner;
import view.framework.ViewController;
import view.menu.Menu;

/**
 *
 * @author Xavi Moreno
 */
public class StatsView extends View {

    private static enum OptionsStats {
        GAME_STATS, PROBLEM_IPS, BACK
    };
    
    private static final String[] descStats = {
        "Game stats",
        "Problematics IPs",
        "Go to back"
    };
    
    public StatsView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "Stats";
    }

    @Override
    public Class run(Scanner sc) {
        Menu<OptionsStats> menu;
        menu = new Menu(OptionsStats.values(), descStats);
        OptionsStats op;
        
        Controller controller = parent.getController();

        do {
            menu.mostrarMenu();
            op = menu.getOpcio(sc);
            switch (op) {
                case GAME_STATS:
                    System.out.println("");
                    controller.getStats().printStatsGames();
                    System.out.println("");
                    break;
                case PROBLEM_IPS:
                    System.out.println("");
                    controller.getStats().printIPProblems();
                    System.out.println("");
                    break;
            }
        } while (op != OptionsStats.BACK);
        return null;
    }
    
}
