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
import ub.swd.model.connection.ProtocolError;
import ub.swd.model.connection.ProtocolError.ErrorType;
import view.Log;

/**
 * Protocol. This class will be responsible to get all messages from the socket
 * and transform them into functions. We are including here a *Connection*
 * @author Pablo
 */
public class Protocol extends AbstractProtocol{
    private ComUtils comUtils;
    private Log log;
    private Game game;
    private onProtocolIOExceptionListener listener;
    
    public Protocol (Socket s, Log l, onProtocolIOExceptionListener listener) throws IOException{
        super(s, ProtocolSide.SERVER_SIDE);
        this.log = l;
        this.comUtils = new ComUtils(s);
        this.game = new Game();
        this.listener = listener;

        
    }
    
    public void setListener(onProtocolIOExceptionListener listener){
        this.listener = listener;
    }

    //-------------------------------------------------------------------------
    //-- READING FUNCTIONS
    //-------------------------------------------------------------------------
    @Override
    public void helloFrameRequest() {
        // This method is called when client wants to start a new game;
        
        Pieces p = this.game.initGame();  // Get the starting pieces
        
        if (game.isPlayerTurn()){         // If is player turn, do nothing.
            helloFrameResponse(p, null);
        }
        else{                                       // If is computer turn, get the first play.
            Object [] o = game.computerTurn();
            DominoPiece d = (DominoPiece) o[0];
            helloFrameResponse(p, d);
        }
    }
    

    @Override
    public void gamePlayRequest(DominoPiece p, Pieces.Side s) {
        // This method is called when the client wants to play a game.
        
        boolean flag = game.throwing(p, s);
        
        if (!flag){     
            // Something went wrong. Report an issue!
            errorResponse(new ProtocolError(ErrorType.ILLEGAL_ACTION_ERR, " INVALID MOVEMENT! Don't do that again :'("+p));
        }
        else{
            // Check if the game's over.
            if (game.isGameOver()){
                // TODO: Get necessary stuff to the response
                gameFinishedResponse(Winner.CLIENT, 0);
            }
            
            // Let the computer play!
            Object [] o = game.computerTurn();
            gamePlayResponse((DominoPiece) o[0], (Side) o[1], game.getNumComputerPieces());
            
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
                gameFinishedResponse(Winner.CLIENT, 0);
            }
            
            gamePlayResponse((DominoPiece) o[0], (Side) o[1], game.getNumComputerPieces());
        }
        else{               // Succeed stealing a piece
            gameStealResponse(dp);
        }
    }

    //-------------------------------------------------------------------------
    //--WRITING FUNCTIONS
    //-------------------------------------------------------------------------
    
    @Override
    public void helloFrameResponse(Pieces hand, DominoPiece compTurn) {
        try {
            super.comUtils.writeByte((byte)0x02);
            super.writePieces(hand);
            super.writeDominoPiece(compTurn);
        } catch (IOException ex) {
            
        }
    }
    
    @Override
    public void gamePlayResponse(DominoPiece p, Side s, int rest) {
         try {
            super.comUtils.writeByte((byte)0x04);
            super.writeDominoPiece(p);
            super.writeSide(s);
            super.comUtils.writeInt32(rest);
            
        } catch (IOException ex) {
            treatException();
        }       
    }
    
    @Override
    public void gameStealResponse(DominoPiece dp) {
        try {
            super.comUtils.writeByte((byte)0x05);
            writeDominoPiece(dp);
        } catch (IOException ex) {
            treatException();
        }
    }


    @Override
    public void gameFinishedResponse(Winner winner, int score) {
         try {
            super.comUtils.writeByte((byte)0x06);
             writeWinner(winner);
             if (Winner.DRAW == winner){
                 super.comUtils.writeInt32(score);
             }
        } catch (IOException ex) {
            treatException();
        }       
    }

    @Override
    public void errorResponse(ProtocolError e) {
        try {
            super.writeErrorType(e.type);
            super.comUtils.writeStringVariable(3,e.msg);
        } catch (IOException ex) {
            treatException();
        }
        
    }
    
    public void treatException(){
        // -- If you arrive here is because client is gone!
        // Let's disconnect him ;)
        listener.onProtocolIOException();
    }
    
    public interface onProtocolIOExceptionListener{
        public abstract void onProtocolIOException();
    }
    
    

}
