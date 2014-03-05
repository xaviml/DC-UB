/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import java.util.ArrayList;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;

/**
 *
 * @author zenbook
 */
public class DominoGame {
    private Pieces mBoard;
    private Pieces mHand;
    
    public DominoGame(ArrayList<DominoPiece> hand) {
        this.mHand = new Pieces(Pieces.ListType.UNSORTED);
        mBoard = new Pieces(Pieces.ListType.SORTED);
    }
    
    public DominoGame(ArrayList<DominoPiece> hand, DominoPiece t) {
        this(hand);
        mBoard.addPiece(t, Pieces.Side.LEFT);
    }
    
    void addTileInBoard(DominoPiece t, Pieces.Side dir) {
        mBoard.addPiece(t, dir);
    }
    
    boolean canSteal() {
        for(DominoPiece t : mHand) {
            if(t.getLeftNumber() == mBoard.getLeftSide() || t.getRightNumber() == mBoard.getLeftSide() ||
               t.getLeftNumber() == mBoard.getRightSide()|| t.getRightNumber() == mBoard.getRightSide())
                return false;
        }
        return true;
    }
    
    public Pieces getHandPieces() { return mHand; }
    public Pieces getBoardPieces() { return mBoard; }
}
