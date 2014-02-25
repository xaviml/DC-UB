/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import java.util.Scanner;
import view.ViewController;

/**
 *
 * @author Xavi Moreno
 */
public class StatsView extends View {

    public StatsView(ViewController parent) {
        super(parent);
    }

    @Override
    public String getTitle() {
        return "stats";
    }

    @Override
    public Class run(Scanner sc) {
        Bundle b = parent.getBundle();
        System.out.println(b.getString("prova"));
        
        sc.next();
        
        return null;
    }
    
}
