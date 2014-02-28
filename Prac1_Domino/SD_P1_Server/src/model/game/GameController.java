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
        t.gameEndFlag = true;   //If both cant move this might be false
        t.scorePlayer = game.getPlayerScore();
        t.scoreComputer = game.getComputerScore();
        return t;
    }


    @Override
    public Turn throwing(DominoPiece piece, Pieces.Side side) {
        Turn t =  game.throwing(piece, side);   // Player plays
        if (t.missMatchFlag || t.invalidMovementFlag) return t;
        
        if (t.gameEndFlag){
            t = endGame(t);
        }
        t = game.computerTurn(t);               // Computer plays
        if (t.gameEndFlag){
            t = endGame(t);
        }
        return t;
    }


    @Override
    public Turn steal() {
        /* Check if game has ended in a Draw*/
        Turn t = game.steal();      // Player play
        
        if (t.playerCantPlayFlag){
            t = game.computerTurn(t);   // Computer play
        }
        if ((t.computerCantPlayFlag && t.playerCantPlayFlag) || t.gameEndFlag){
            return endGame(t);
        }
        
        return t;
    }

    @Override
    public Turn initGame() {
        return game.initGame();
    }
    
}
