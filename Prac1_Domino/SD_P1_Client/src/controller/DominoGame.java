/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import java.util.ArrayList;
import model.Board;
import model.Board.Dir;
import model.Tile;

/**
 *
 * @author zenbook
 */
public class DominoGame {
    private Board mBoard;
    private ArrayList<Tile> mHand;
    
    public DominoGame(ArrayList<Tile> hand) {
        this.mHand = hand;
        mBoard = new Board();
    }
    
    public DominoGame(ArrayList<Tile> hand, Tile t) {
        this(hand);
        mBoard.addTileInBoard(t, Dir.LEFT);
    }
    
    public void addTileInBoard(Tile t, Dir dir) {
        mBoard.addTileInBoard(t, dir);
    }
    
    public boolean canSteal() {
        for(Tile t : mHand) {
            if(t.getLeftNumber() == mBoard.getLeftSide() || t.getRightNumber() == mBoard.getLeftSide() ||
               t.getLeftNumber() == mBoard.getRightSide()|| t.getRightNumber() == mBoard.getRightSide())
                return false;
        }
        return true;
    }
}
