/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import view.Log;

/**
 *
 * @author Pablo
 */
public class GameController implements GameControllerInterface{
    Log log;
    Game game;
    
    public GameController(Log log){
        this.game = new Game();
        this.log = log;
    }

    @Override
    public Turn endGame(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


    @Override
    public Turn throwing(DominoPiece piece, Pieces.Side side) {
        Turn t =  game.throwing(piece, side);
        if (t.gameEndFlag){
            t = endGame(t);
        }
        return t;
    }


    @Override
    public Turn steal() {
        /* Check if game has ended in a Draw*/
        Turn t = game.steal();
        if ((t.computerCantPlayFlag && t.playerCantPlayFlag) || t.gameEndFlag){
            return endGame(t);
        }
        
        return game.steal();
    }

    @Override
    public Turn initGame() {
        return game.initGame();
    }
    
}
