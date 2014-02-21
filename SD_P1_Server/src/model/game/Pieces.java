/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Pablo
 */
public class Pieces {
    public static enum ListType{SORTED, UNSORTED};
    public static enum Side{LEFT,RIGHT};
    
    private ListType listType;
    private ArrayList<DominoPiece> list;
    private int rn, ln;
    
    public Pieces(ListType type){
        this.listType = type;
        this.list = new ArrayList<>();
    }
    
    public ArrayList<DominoPiece> viewPieces(){
        return list;
    }
    
    /**
     * This function returns a random item from the list, and removes it
     * from the list.
     * @return DominoPiece, Or Null if list is empty
     */
    public DominoPiece takeRandomPiece(){

        if (!list.isEmpty()){
            Random randomGenerator = new Random();
            int index = randomGenerator.nextInt(list.size());

            return list.remove(index);
        }
        return null;
    }
    
    public void addPiece(DominoPiece piece){
        list.add(piece);
    }
    
    public void addPiece(DominoPiece piece, Side side){
        if(listType == ListType.UNSORTED){
            // Unsorted lists cannot use this method;
            return;
        }
        
        if (side == Side.RIGHT){ // Add to the right side of the list;
            
            // Check if is possible to add the piece
            // (one || two nuber/s equals to "rn" = Right number)
            
            // Add it to the list and change "rn"
            
        }
        else { // Add to the left side of the list;
            
            // Check if is possible to add the piece
            // (one || two nuber/s equals to "ln" = Left number)
            
            // Add it to the list and change "ln")
        }
    }
    
}
