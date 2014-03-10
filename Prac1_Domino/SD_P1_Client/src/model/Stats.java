/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model;

import java.util.ArrayList;

/**
 *
 * @author Xavi Moreno
 */
public class Stats {

    private ArrayList<StatMatch> table;
    private ArrayList<String> problemsIP;
    
    public Stats() {
        table = new ArrayList<>();
        problemsIP = new ArrayList<>();
    }
    
    public void addStat(StatMatch s) {
        table.add(s);
    }
    
    public void addProblemIP(String ip) {
        if(!problemsIP.contains(ip))
            problemsIP.add(ip);
    }

    public void printStatsGames(){
        if(table.isEmpty()) {
            System.out.println("There aren't results");
            return;
        }
        String out = "   You   Draw   Comp   IPcomp\n";
        for (int i = 0; i < table.size(); i++) {
            switch (table.get(i).winner) {
                case CLIENT:
                    out += (i+1)+"   X                  "+table.get(i).ipServer;
                    break;
                case SERVER:
                    out += (i+1)+"               X      "+table.get(i).ipServer;
                    break;
                case DRAW:
                    out += (i+1)+"         X            "+table.get(i).ipServer;
                    break;
            }
        }
        System.out.println(out);
    }
    
    public void printIPProblems() {
        System.out.println("IPs");
        for (int i = 0; i < this.problemsIP.size(); i++) {
           System.out.println("\t"+(i+1)+".- "+this.problemsIP.get(i));
            
        }
        for (String ip : this.problemsIP) {
            
        }
    }
    
    
}
