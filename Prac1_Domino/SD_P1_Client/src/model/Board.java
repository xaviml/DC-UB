/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model;

import java.util.ArrayList;

/**
 *
 * @author Xavi Moreno
 */
public class Board {

    public static enum Dir {LEFT, RIGHT};
    
    private ArrayList<Tile> mBoard;
    private int right;
    private int left;
    
    public Board() {
        mBoard = new ArrayList<>();
        right = -1;
        left = -1;
    }

    public int getRightSide() {
        return right;
    }

    public int getLeftSide() {
        return left;
    }
    
    public void addTileInBoard(Tile t, Dir dir) {
        if(dir == Dir.LEFT) {
            this.mBoard.add(0, t);
            
        }else {
            this.mBoard.add(t);
        }
        left = this.mBoard.get(0).getLeftNumber();
        right = this.mBoard.get(this.mBoard.size()-1).getRightNumber();
    }
    
    
    
}
