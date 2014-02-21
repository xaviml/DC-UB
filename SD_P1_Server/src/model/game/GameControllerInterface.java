/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

/**
 *
 * @author Pablo
 */
public interface GameControllerInterface {
    
    abstract void endGame();
    abstract Turn throwing(DominoPiece piece, Pieces.Side side);
    abstract Turn steal();
    abstract Turn initGame();
}
