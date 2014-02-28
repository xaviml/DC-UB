/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Pablo
 */
public class Pieces implements Iterable{

    boolean contains(DominoPiece piece) {
        return list.contains(piece);
    }





    public static enum ListType{SORTED, UNSORTED};
    public static enum Side{LEFT,RIGHT};
    
    private ListType listType;
    private ArrayList<DominoPiece> list;
    private int nr, nl;
    
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
    
    public ArrayList<DominoPiece> getList() {
        return list;
    }
    
    public void removePiece(DominoPiece dp) {
        list.remove(dp);
    }
    
    public boolean addPiece(DominoPiece piece, Side side){

        if(listType == ListType.UNSORTED){
            // Unsorted lists cannot use this method;
            return false;
        }
        if (list.isEmpty()){
            list.add(0, piece);
            this.nl = piece.nl;
            this.nr = piece.nr;
            return true;
        }
        
        if (side == Side.LEFT){ // Add to the right side of the list;
            
            // Check if is possible to add the piece
            // (one || two nuber/s equals to "rn" = Right number)
            if (piece.nr == this.nl || piece.nl == this.nl){
                if (piece.nl == this.nl) piece.reverse();
                
                list.add(0, piece);
                this.nl = piece.nl;
                /*
                // TEST STUFF
                System.out.println("===============");
                for (DominoPiece dp: list){
                    System.out.println(dp);
                }
                System.out.println("===============");
                System.out.println("NUMBER LEFT: "+this.nl);
                System.out.println("NUMBER RIGHT: "+this.nr);
                //*/
                
                return true;
            }
            return false;
            // Add it to the list and change "rn"
            
        }
        else { // Add to the left side of the list;
            
            // Check if is possible to add the piece
            // (one || two nuber/s equals to "ln" = Left number)
            
            if (piece.nl == this.nr || piece.nr == this.nr){
                if (piece.nr == this.nr) piece.reverse();
                
                list.add(piece);
                this.nr = piece.nr;
                /*
                // TEST STUFF
                System.out.println("===============");
                for (DominoPiece dp: list){
                    System.out.println(dp);
                }
                System.out.println("===============");
                System.out.println("NUMBER LEFT: "+this.nl);
                System.out.println("NUMBER RIGHT: "+this.nr);
                //*/
                return true;
            }
            return false;
            // Add it to the list and change "ln")
        }
    }
    
    public int getSize(){
        return list.size();
    }
    
    @Override
    public Iterator iterator() {
        return list.iterator();
    }

}
