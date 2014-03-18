/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.menu;

import java.util.Scanner;

/**
 * Classe Menú de soport
 * @author Pablo Martinez i Xavi Moreno
 * @param <E>
 */
public class Menu<E extends Enum<E>> {

    private E[] llistaOpcions;
    private String[] llistaDescripcions;

    public Menu(E[] llistaOpcions, String[]  llistaDescripcions) {
        this.llistaOpcions = llistaOpcions;
        this.llistaDescripcions = llistaDescripcions;
    }

    /**
     * This function shown the menu on the screen.
     */
    public void mostrarMenu() {
        for (int i = 0; i < getMaxLen(); i++) {
            System.out.println((i+1)+".- "+llistaDescripcions[i]);
        }
    }
    
    /**
     * This function returns the options chosen
     * @param sc
     * @return 
     */
    public E getOpcio(Scanner sc) {
        int op = 0;
        boolean sortir = false;
        while(!sortir) {
            System.out.print("\nChoose an option: ");
            String opS = sc.nextLine();
            try{
                op = Integer.parseInt(opS);
                if(op>getMaxLen() || op<=0) {
                    System.out.println("This options doesn't exist.");
                }else{
                    sortir = true;
                }
            }catch(NumberFormatException ex) {
                System.out.println("Write a number option please.");
            }
            
        }
        return llistaOpcions[op-1];
        
        
    }

    private int getMaxLen() {
        return llistaOpcions.length;
    }
}
