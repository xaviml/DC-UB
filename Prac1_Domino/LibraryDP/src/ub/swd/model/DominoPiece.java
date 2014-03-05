/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.swd.model;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author Xavi Moreno
 */
public class DominoPiece {
    private int[] n;
    
    public DominoPiece(int left, int right) {
        n = new int[]{left, right};
    }
    
    public void revert() {
        int aux = n[0];
        n[0] = n[1];
        n[1] = aux;
    }
    
    public int getLeftNumber() {
        return n[0];
    }
    
    public int getRightNumber() {
        return n[1];
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
        
       int scorethis = (this.isDouble())? this.n[0]+100 : this.n[0]+n[1];
       int scorepiece = (piece.isDouble())? piece.n[0]+100 : piece.n[0]+piece.n[1];
       
       return (scorepiece>scorethis);
    }
    
    /**
     * Tell me if this piece is double
     * @return 
     */
    public boolean isDouble(){
        return this.n[0] == this.n[1];
    }
    
    @Override
    public int hashCode() {
        // With this hashcode each piece will have a unique identifier
        return max(n[1],n[0])*10+min(n[1],n[0]);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DominoPiece){
            DominoPiece dp = (DominoPiece) obj;
            return ((this.n[1] == dp.n[1] && this.n[0] == dp.n[0]) || (this.n[0] == dp.n[1] && this.n[1] == dp.n[0]));
        }
        return false;
    }
    
    @Override
    public String toString(){
        if (isDouble()){
            return "+---------------+\n"
                    + "|       |       |\n"
                    + "|   "+n[0]+"   |   "+n[1]+"   |\n"
                    + "|       |       |\n"
                    + "+---------------+\n";
        }
        else{
            return "+-----+\n"
                    + "|     |\n"
                    + "|  "+n[0]+"  |\n"
                    + "|_____|\n"
                    + "|     |\n"
                    + "|  "+n[1]+"  |\n"
                    + "|     |\n"
                    + "+-----+\n";
        }
    }
}
