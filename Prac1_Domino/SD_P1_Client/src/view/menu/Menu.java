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
     * Mètode per mostrar el menú
     */
    public void mostrarMenu() {
        for (int i = 0; i < getMaxLen(); i++) {
            System.out.println((i+1)+".- "+llistaDescripcions[i]);
        }
    }
    /**
     * Mètode que retorna la opció escollida
     * @param sc
     * @return 
     */
    public E getOpcio(Scanner sc) {
        int op = 0;
        boolean sortir = false;
        while(!sortir) {
            System.out.print("\nEscull una opció: ");
            op = sc.nextInt();
            sc.nextLine();
            if(op>getMaxLen() || op<=0) {
                System.err.println("Aquest nombre no està en aquest menú");
            }else{
                sortir = true;
            }
        }
        return llistaOpcions[op-1];
        
        
    }

    private int getMaxLen() {
        return llistaOpcions.length;
    }
}
