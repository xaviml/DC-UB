/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;

/**
 * This class contains the game information.
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
    
    /**
     * This function adds a tile in board.
     * 
     * @param t
     * @param dir 
     */
    
    public void addTileInBoard(DominoPiece t, Pieces.Side dir) {
        mBoard.addPiece(t,dir);
    }
    
    /**
     * This functions allows to throw a tile in board
     * 
     * @param dp
     * @param dir 
     */
    
    public void throwTile(DominoPiece dp, Pieces.Side dir) {
        mHand.removePiece(dp);
        addTileInBoard(dp, dir);
    }
    
    /**
     * Gets a list of pieces that the player can throw.
     * 
     * @return 
     */
    
    public Pieces getPossiblePiecesCanThrow() {
        Pieces out = new Pieces(Pieces.ListType.UNSORTED);
        for (DominoPiece dp : mHand) {
            if(canJoinToBoard(dp))
                out.addPiece(dp);
        }
        return out;
    }
    
    /**
     * Return true if the first movement hasn't been realized yet.
     * Otherwise return false.
     * 
     * @return 
     */
    
    public boolean isFirstMovement() {
        return mBoard.getLeftSide() == -1 && mBoard.getRightSide() == -1;
    }
    
    /**
     * This function returns true if the tile can be placed on that side.
     * 
     * @param dp
     * @param s
     * @return 
     */
    
    public boolean canPutTileOnBoard(DominoPiece dp, Side s) {
        if(isFirstMovement()) { //first movement
            return true;
        }
        if(s == Side.LEFT)
            return dp.getRightNumber() == mBoard.getLeftSide();
        else
            return dp.getLeftNumber() == mBoard.getRightSide();
    }
    
    /**
     * Return 'true' if the player can steal a tile. Otherwise returns false.
     * 
     * @return 
     */
    
    public boolean canSteal() {
        for(DominoPiece t : mHand) {
            if(canJoinToBoard(t))
                return false;
        }
        return true;
    }
    
    /**
     * This function returns 'true' if that tile can be placed on the board.
     * 
     * @param dp
     * @return 
     */
    
    public boolean canJoinToBoard(DominoPiece dp) {
        if(isFirstMovement()) { //first movement
            return mHand.getBestPiece().equals(dp);
        }else{
            return dp.getLeftNumber() == mBoard.getLeftSide() || dp.getRightNumber() == mBoard.getLeftSide() ||
                dp.getLeftNumber() == mBoard.getRightSide()|| dp.getRightNumber() == mBoard.getRightSide();
        }
    }
    
    /**
     * Gets the hand of player
     * 
     * @return 
     */
    
    public Pieces getHandPieces() { return mHand; }
    
    /**
     * Gets the board
     * 
     * @return 
     */
    
    public Pieces getBoardPieces() { return mBoard; }
}
