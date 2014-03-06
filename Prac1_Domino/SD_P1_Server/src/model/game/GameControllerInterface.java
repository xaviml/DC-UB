/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;

/**
 *
 * @author Pablo
 */
public interface GameControllerInterface {
    public enum GameState{STARTING, COMP_TURN, PLAYER_TURN, FINISHED};
    
    
    abstract boolean isGameOver();
    abstract Object [] computerTurn();
    abstract void endGame();
    abstract boolean throwPiece(DominoPiece piece, Pieces.Side side);
    abstract DominoPiece steal();
    abstract Pieces initGame();
}
