/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model.connection;

import ub.swd.model.connection.ComUtils;
import java.io.IOException;
import java.net.Socket;
import model.game.Game;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;
import ub.swd.model.connection.AbstractProtocol;
import ub.swd.model.connection.Error;
import ub.swd.model.connection.Error.ErrorType;
import view.Log;

/**
 * Protocol. This class will be responsible to get all messages from the socket
 * and transform them into functions. We are including here a *Connection*
 * @author Pablo
 */
public class Protocol extends AbstractProtocol{
    private ComUtils com;
    private Log log;
    private Game game;
    
    public Protocol (Socket s, Log l) throws IOException{
        super(s, ProtocolSide.SERVER_SIDE);
        this.log = l;
        try {
            this.com = new ComUtils(s);
        } catch (IOException ex) {
            System.err.println("BAD");
        }
        this.game = new Game();

        
    }

    @Override
    public void helloFrameRequest() {
        // This method is called when client wants to start a new game;
        
        Pieces p = this.game.initGame();  // Get the starting pieces
        
        if (game.isPlayerTurn()){         // If is player turn, do nothing.
            helloFrameResponse(p, null, null);
        }
        else{                                       // If is computer turn, get the first play.
            Object [] o = game.computerTurn();
            DominoPiece d = (DominoPiece) o[0];
            Side s = (Side) o[1];
            helloFrameResponse(p, d, s);
        }
    }
    

    @Override
    public void gamePlayRequest(DominoPiece p, Pieces.Side s) {
        
        boolean flag = game.throwing(p, s);
        
        if (!flag){     
            // Something went wrong. Report an issue!
            errorResponse(ErrorType.ILLEGAL_ACTION_ERR, "That was not a valid action!");
        }
        else{
            // Check if the game's over.
            if (game.isGameOver()){
                // TODO: Get necessary stuff to the response
                gameFinishedResponse(0, 0);
            }
            
            // Let the computer play!
            Object [] o = game.computerTurn();
            gamePlayResponse((DominoPiece) o[0], (Side) o[1]);
            
        }
    }

    @Override
    public void gameStealRequest() {
        // Steal
        DominoPiece dp = game.steal();
        if (dp == null){    // No pieces in the resto!
            Object [] o = game.computerTurn();
            // Check if computer can play!
            if (o[0] == null){ // Finish the game
                game.endGame();
                
                // TODO: Score and stuff..
                gameFinishedResponse(0, 0);
            }
            
            gamePlayResponse((DominoPiece) o[0], (Side) o[1], game.getNumComputerPieces());
        }
        else{               // Succeed stealing a piece
            gameStealResponse(dp);
        }
    }

    @Override
    public void gameStealResponse(DominoPiece dp) {
        // WRITE METHOD!
    }

    
    

    @Override
    public void errorResponse(ErrorType e, String s) {
        // WRITE METHOD!
    }

    @Override
    public void helloFrameResponse(Pieces hand, DominoPiece compTurn) {
        
    }

    @Override
    public void gamePlayResponse(DominoPiece p, Side s, int rest) {
        
    }

    @Override
    public void gameFinishedResponse(Winner winner, int score) {
        
        // TODO: After this, close the socket! -- =)
    }

}
