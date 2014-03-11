/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model.connection;

import ub.swd.model.connection.ComUtils;
import java.io.IOException;
import java.net.Socket;
import model.Constants;
import model.game.Game;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;
import ub.swd.model.connection.AbstractProtocol;
import ub.swd.model.connection.ProtocolError;
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
    private int errorMargin = 0;
    private boolean canPlay;
    
    public Protocol (Socket s, boolean canPlay, Log l) throws IOException{
        super(s, ProtocolSide.SERVER_SIDE);

        this.canPlay = canPlay;
        this.log = l;
        this.comUtils = new ComUtils(s);
        this.game = new Game();
        
    }
    

    //-------------------------------------------------------------------------
    //-- READING FUNCTIONS
    //-------------------------------------------------------------------------
    @Override
    public void helloFrameRequest() throws IOException{
        /* Check if we can serve this new connection */
        if (!canPlay){
            errorResponse(new ProtocolError(ProtocolError.ErrorType.NOT_ENOUGH_RESOURCES_ERR, "Server is full! Try again later."));
            throw new IOException();
        }
        
        /* Check if this message is expected */
        if (this.game.getSate() != Game.GameState.STARTING){
            errorResponse(new ProtocolError(ProtocolError.ErrorType.SYNTAX_ERR, "'0x01' is a helloFrameRequest message. you can't send this now."));
            return;
        }
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
    public void gamePlayRequest(DominoPiece p, Pieces.Side s) throws IOException{
        /* Check if we can serve this new connection */
        if (!canPlay){
            errorResponse(new ProtocolError(ProtocolError.ErrorType.NOT_ENOUGH_RESOURCES_ERR, "Server is full! Try again later."));
            throw new IOException();
        }        
    
        /* Check if this message is expected */
        if (this.game.getSate() != Game.GameState.PLAYER_TURN){
            errorResponse(new ProtocolError(ProtocolError.ErrorType.SYNTAX_ERR, "'0x03' is a gamePlayRequest message. you can't send this now."));
            return;
        }
        // This method is called when the client wants to play a game.
    
        Game.ThrowResult flag = game.throwing(p, s);
        
        if (flag != Game.ThrowResult.SUCCESS){     
            // Something went wrong. Report an issue!
            switch(flag){
                case NOT_FIT:
                    errorResponse(new ProtocolError(ProtocolError.ErrorType.ILLEGAL_ACTION_ERR, "Piece: "+p+" doesn't fit on "+s+"."));
                    break;
                case NOT_IN_HAND:
                    errorResponse(new ProtocolError(ProtocolError.ErrorType.ILLEGAL_ACTION_ERR, "You don't have the piece "+p+" in your hand."));
                    break;
                case NOT_YOUR_BEST:
                    errorResponse(new ProtocolError(ProtocolError.ErrorType.ILLEGAL_ACTION_ERR, "In the first throw you must place your best piece."));
                    break;
            }
        }
        else{
            // Check if the game's over.
            if (game.isGameOver()){
                gameFinishedResponse(game.getWinner(), game.getComputerScore());
                return;
            }
            
            // Let the computer play!
            Object [] o = game.computerTurn();
            gamePlayResponse((DominoPiece) o[0], (Side) o[1], game.getNumComputerPieces());
            
            // Check if the game's over
            if (game.isGameOver()){
                gameFinishedResponse(game.getWinner(), game.getComputerScore());
            }
            
        }
    }

    @Override
    public void gameStealRequest() throws IOException{
        /* Check if we can serve this new connection */
        if (!canPlay){
            errorResponse(new ProtocolError(ProtocolError.ErrorType.NOT_ENOUGH_RESOURCES_ERR, "Server is full! Try again later."));
            throw new IOException();
        }
        
        /* Check if this message is expected */
        if (this.game.getSate() != Game.GameState.PLAYER_TURN){
            errorResponse(new ProtocolError(ProtocolError.ErrorType.SYNTAX_ERR, "'0x03' is a gamePlayRequest message. you can't send this now."));
            return;
        }

        /* Check if the player can steal */
        if (!game.playerCanSteal()){
            errorResponse(new ProtocolError(ProtocolError.ErrorType.ILLEGAL_ACTION_ERR, "If you have pieces that fit in the game you cannot steal."));
            return;
        }
        // Steal
        DominoPiece dp = game.steal();
        if (dp == null){    // No pieces in the resto!
            Object [] o = game.computerTurn();
            // Check if computer can play!
            if (o[0] == null){ // Finish the game
                game.endGame();
                
                gameFinishedResponse(game.getWinner(), game.getComputerScore());
                return;
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
    public void helloFrameResponse(Pieces hand, DominoPiece compTurn) throws IOException{
        super.comUtils.writeByte((byte)0x02);
        super.writePieces(hand);
        super.writeDominoPiece(compTurn);
    }
    
    @Override
    public void gamePlayResponse(DominoPiece p, Side s, int rest) throws IOException{
        super.comUtils.writeByte((byte)0x04);
        super.writeDominoPiece(p);
        super.writeSide(s);
        super.comUtils.writeInt32(rest);
    }
    
    @Override
    public void gameStealResponse(DominoPiece dp) throws IOException{
        super.comUtils.writeByte((byte)0x05);
        writeDominoPiece(dp);
    }


    @Override
    public void gameFinishedResponse(Winner winner, int score) throws IOException{
        log.write(this.getClass().getSimpleName(), "Game finished. The winner was: "+winner+". The scores were: Pla:"+game.getPlayerScore()+" | Com:"+game.getComputerScore()+".", Log.MessageType.GAME);
        super.comUtils.writeByte((byte)0x06);
         writeWinner(winner);
         if (Winner.DRAW == winner){
             super.comUtils.writeInt32(score);
         }
    }

    @Override
    public void errorResponse(ProtocolError e) throws IOException{
        /* Increase the error counter. ILLEGAL_ACTIONS doesn't count as protocol errors*/
        if (e.type != ProtocolError.ErrorType.ILLEGAL_ACTION_ERR) errorMargin+=1;

        /* Check if the client has reached the max ammount of protocol errors*/
        if (errorMargin >= Constants.PROTOCOL_ERRORS_PER_CONNECTION){
            /* Notify and disconnect */
            comUtils.writeByte((byte)0x00);
            super.writeErrorType(ProtocolError.ErrorType.UNDEFINED_ERR);
            super.comUtils.writeStringVariable(3,"You have reached the limit of protocol errors per connection. You are being disconnected.");            
            
            this.log.write(this.getClass().getSimpleName(), "Force quit due to many protocol errors.", Log.MessageType.CONNECTION);
            /* We might create a new Exception, anyway no info is needed, so its ok with IOEx */
            throw new IOException();
        }
        comUtils.writeByte((byte)0x00);
        super.writeErrorType(e.type);
        super.comUtils.writeStringVariable(3,e.msg);
        this.log.write(this.getClass().getSimpleName(), "ERROR->msg: "+e.msg, Log.MessageType.ERROR);
        
    }

    public Game getGame() {
        return game;
    }

}
