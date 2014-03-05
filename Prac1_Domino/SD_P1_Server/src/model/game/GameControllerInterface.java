/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.connection.Turn;

/**
 *
 * @author Pablo
 */
public interface GameControllerInterface {
    
    abstract Turn endGame(Turn t);
    abstract Turn throwing(DominoPiece piece, Pieces.Side side);
    abstract Turn steal();
    abstract Turn initGame();
}
