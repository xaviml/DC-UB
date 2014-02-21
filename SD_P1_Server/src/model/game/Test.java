/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

/**
 *
 * @author Pablo
 */
public class Test {
    public static void main(String [] args){
        DominoPiece p = new DominoPiece(2,6);
        System.out.println(p.toString());
        p = new DominoPiece(6, 6);
        System.out.println(p.toString());
        p = new DominoPiece(6, 5);
        System.out.println(p.toString());
        p = new DominoPiece(5,4);
        System.out.println(p.toString());
    }
    
}
