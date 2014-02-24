/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author Pablo
 */
public class DominoPiece {
    int nr,nl;
    
    public DominoPiece(int nr, int nl){
        this.nr = nr;
        this.nl = nl;
    }
    
    public void reverse(){
        int n = this.nl;
        this.nl = this.nr;
        this.nr = n;
    }
    
    @Override
    public String toString(){
        if (nr == nl){
            return " _______\n"+"| "+nl+" | "+nr+" |"+"\n|___|___|";
        }
        else{
            return " ___\n"+"| "+nl+" |\n"+"|___|\n"+"| "+nr+" |\n"+"|___|";
        }
    }
    
    
    /**
     * Method that gets the best of two pieces at start.
     * @param piece
     * @return true if this is better, false if not.
     */
    public boolean isThisBetter(DominoPiece piece){
        if (piece == null){
            return false;
        }
        
       int scorethis = (this.isDouble())? this.nl+100 : this.nl+nr;
       int scorepiece = (piece.isDouble())? piece.nl+100 : piece.nl+piece.nr;
       
       return (scorepiece>scorethis);
    }
    
    /**
     * Tell me if this piece is double
     * @return 
     */
    public boolean isDouble(){
        return this.nl == this.nr;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DominoPiece){
            DominoPiece dp = (DominoPiece) obj;
            return ((this.nr == dp.nr && this.nl == dp.nl) || (this.nl == dp.nr && this.nr == dp.nl));
        }
        return false;
        
    }

    @Override
    public int hashCode() {
        // With this hashcode each piece will have a unique identifier
        return max(nr,nl)*10+min(nr,nl);
    }
    
}
