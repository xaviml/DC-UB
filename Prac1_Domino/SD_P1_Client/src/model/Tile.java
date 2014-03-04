/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model;

/**
 *
 * @author Xavi Moreno
 */
public class Tile {
    public int[] mNumbers;
    
    public Tile(int left, int right) {
        mNumbers = new int[]{left, right};
    }
    
    public void revert() {
        int aux = mNumbers[0];
        mNumbers[0] = mNumbers[1];
        mNumbers[1] = aux;
    }
    
    public int getLeftNumber() {
        return mNumbers[0];
    }
    
    public int getRightNumber() {
        return mNumbers[1];
    }

}
