/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model;

import java.util.ArrayList;

/**
 * This class contains tables of result.
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
    
    /**
     * Allows to add a new stat
     * 
     * @param s 
     */
    
    public void addStat(StatMatch s) {
        table.add(s);
    }
    
    /**
     * Adds a new problematic IP
     * 
     * @param ip 
     */
    
    public void addProblemIP(String ip) {
        if(!problemsIP.contains(ip))
            problemsIP.add(ip);
    }
    
    /**
     * This function prints a table on the screen.
     * 
     */

    public void printStatsGames(){
        if(table.isEmpty()) {
            System.out.println("There aren't results");
            return;
        }
        String out = "   You   Draw   Comp   IPcomp\n";
        for (int i = 0; i < table.size(); i++) {
            switch (table.get(i).winner) {
                case CLIENT:
                    out += (i+1)+"   X                  ";
                    break;
                case SERVER:
                    out += (i+1)+"               X      ";
                    break;
                case DRAW:
                    out += (i+1)+"         X            ";
                    break;
            }
            out+=table.get(i).ipServer+"\n";
        }
        System.out.println(out);
    }
    
    /**
     * This function prints a table on the screen.
     * 
     */
    
    public void printIPProblems() {
        System.out.println("Problematic IPs");
        for (int i = 0; i < this.problemsIP.size(); i++) {
           System.out.println("\t"+(i+1)+".- "+this.problemsIP.get(i));
            
        }
    }
    
    
}
