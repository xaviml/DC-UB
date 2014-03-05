/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import view.framework.View;
import view.framework.Bundle;
import java.util.Scanner;
import view.framework.ViewController;

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
        System.out.println(b.getString("stats"));
        
        sc.next();
        
        return null;
    }
    
}
