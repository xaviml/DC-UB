/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import view.Log;

/**
 *
 * @author Pablo
 */
public class GameController implements GameControllerInterface{
    private Log log;
    private Game game;
    private GameState gameState;
    
    public GameController(Log log){

        this.game = new Game();
        this.log = log;
        
    }

    @Override
    public boolean isGameOver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean throwPiece(DominoPiece piece, Pieces.Side side) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DominoPiece steal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Pieces initGame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Object[] computerTurn() {
        throw new UnsupportedOperationException("Later");
    }
}
