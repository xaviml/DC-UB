/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;

/**
 *
 * @author zenbook
 */
public class DominoGame {
    private Pieces mBoard;
    private Pieces mHand;
    
    public DominoGame(Pieces hand) {
        this.mHand = hand;
        mBoard = new Pieces(Pieces.ListType.SORTED);
    }
    
    public DominoGame(Pieces hand, DominoPiece t) {
        this(hand);
        mBoard.addPiece(t, Pieces.Side.LEFT);
    }
    
    public void addTileInBoard(DominoPiece t, Pieces.Side dir) {
        mBoard.addPiece(t,dir);
    }
    
    public void throwTile(DominoPiece dp, Pieces.Side dir) {
        mHand.removePiece(dp);
        addTileInBoard(dp, dir);
    }
    
    public Pieces getPossiblePiecesCanThrow() {
        Pieces out = new Pieces(Pieces.ListType.UNSORTED);
        for (DominoPiece dp : mHand) {
            if(canJoinToBoard(dp))
                out.addPiece(dp);
        }
        return out;
    }
    
    public boolean isFirstMovement() {
        return mBoard.getLeftSide() == -1 && mBoard.getRightSide() == -1;
    }
    
    public boolean canPutTileOnBoard(DominoPiece dp, Side s) {
        if(isFirstMovement()) { //first movement
            return true;
        }
        if(s == Side.LEFT)
            return dp.getRightNumber() == mBoard.getLeftSide();
        else
            return dp.getLeftNumber() == mBoard.getRightSide();
    }
    
    public boolean canSteal() {
        for(DominoPiece t : mHand) {
            if(canJoinToBoard(t))
                return false;
        }
        return true;
    }
    
    public boolean canJoinToBoard(DominoPiece dp) {
        if(isFirstMovement()) { //first movement
            return mHand.getBetterPiece().equals(dp);
        }else{
            return dp.getLeftNumber() == mBoard.getLeftSide() || dp.getRightNumber() == mBoard.getLeftSide() ||
                dp.getLeftNumber() == mBoard.getRightSide()|| dp.getRightNumber() == mBoard.getRightSide();
        }
    }
    
    public Pieces getHandPieces() { return mHand; }
    public Pieces getBoardPieces() { return mBoard; }
}
