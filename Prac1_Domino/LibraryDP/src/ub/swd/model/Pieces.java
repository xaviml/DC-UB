/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.swd.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author Pablo
 */
public class Pieces implements Iterable<DominoPiece>{

    public static enum ListType{SORTED, UNSORTED};
    public static enum Side{LEFT,RIGHT};
    
    private ListType listType;
    private ArrayList<DominoPiece> list;
    private int nr, nl;
    
    public Pieces(ListType type){
        this.listType = type;
        this.list = new ArrayList<>();
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
    
    public ArrayList<DominoPiece> getPieces() {
        return list;
    }
    
    public int getRightSide() {
        return nr;
    }

    public int getLeftSide() {
        return nl;
    }
    
    public void removePiece(DominoPiece dp) {
        list.remove(dp);
    }
    
    public void addPiece(DominoPiece piece){
        if(listType == ListType.UNSORTED)
            list.add(piece);
    }
    
    public void prueba() {}
    
    public boolean addPiece(DominoPiece piece, Side side) {
        boolean added = false;
        if(listType == ListType.SORTED){
            if(side == Side.LEFT) {
                if(list.get(0).getLeftNumber() == piece.getRightNumber()) {
                    this.list.add(0, piece);
                    added = true;
                } else
                    added = false;
            }else {
                if(list.get(0).getRightNumber() == piece.getLeftNumber()) {
                    this.list.add(piece);
                    added = true;
                } else
                    added = false;
            }
            if(added) {
                nl = this.list.get(0).getLeftNumber();
                nr = this.list.get(this.list.size()-1).getRightNumber();
            }
        }
        return added;
    }
    
    public int getNumPieces(){
        return list.size();
    }
    
    public boolean contains(DominoPiece piece) {
        return list.contains(piece);
    }
    
    @Override
    public Iterator iterator() {
        return list.iterator();
    }
}
