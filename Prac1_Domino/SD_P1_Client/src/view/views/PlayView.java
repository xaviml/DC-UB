/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import java.util.Scanner;
import view.ViewController;

/**
 *
 * @author zenbook
 */
public class PlayView extends View{

    public PlayView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "Play";
    }

    @Override
    public Class run(Scanner sc) {
        sc.next();
        Bundle b = parent.getBundle();
        b.putString("stats", "Xavii!");
        return StatsView.class;
    }
    
}
