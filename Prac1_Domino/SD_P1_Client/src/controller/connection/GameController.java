/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller.connection;

import client.Constants;
import controller.DominoGame;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;
import ub.swd.model.connection.AbstractProtocol;
import ub.swd.model.connection.ProtocolError;

/**
 *
 * @author Xavi Moreno
 */
public class GameController extends AbstractProtocol{
    
    private DominoGame mGame;
    private OnServerResponseListener listener;
    
    private boolean pieceAdded;

    public GameController(Socket socket, ProtocolSide side) throws IOException {
        super(socket, side);
        socket.setSoTimeout(Constants.TIMEOUT); //set 5 seconds oftimeout 
    }
    
    public void setOnServerResponseListener(OnServerResponseListener l) {
        this.listener = l;
    }

    public DominoGame getGame() {
        return mGame;
    }
    
    @Override
    public void helloFrameRequest() {
        try {
            //HEADER
            comUtils.writeByte((byte) 0x01);
            //NO BODY
            
            //Read response
            readFrame();
        } catch (IOException ex) {
            if(listener != null)
                listener.errorIO();
        }
    }
    
    @Override
    public void helloFrameResponse(Pieces hand, DominoPiece compTurn) {
        if(compTurn == null) { //means that client start
            mGame = new DominoGame(hand);
        } else {
            mGame = new DominoGame(hand, compTurn);
        }
        if(listener != null)
            listener.initTiles(hand, (compTurn == null));
    }

    @Override
    public void gamePlayRequest(DominoPiece p, Pieces.Side s) {
        try {
            //HEADER
            comUtils.writeByte((byte)0x03);
            //BODY
            writeDominoPiece(p);
            writeSide(s);
            
            //Read response
            readFrame();
            if(pieceAdded)
                mGame.throwTile(p, s);
                
        } catch (IOException ex) {
            if(listener != null)
                listener.errorIO();
        }
    }
    
    @Override
    public void gamePlayResponse(DominoPiece p, Pieces.Side s, int rest) {
        //Si rest es igual a 0, leer trama
        pieceAdded = true;
        mGame.addTileInBoard(p, s);
        if(listener != null)
            listener.throwResponse(p, rest);
        
        if(rest == 0) {
            try {
                readFrame();
            } catch (IOException ex) {
                if(listener != null)
                    listener.errorIO();
            }
        }
    }

    @Override
    public void gameStealRequest() {
        try {
            //HEADER
            comUtils.writeByte((byte)0x03);
            //BODY
            writeNoMovementFrame();
            
            //Read response
            readFrame();
        } catch (IOException ex) {
            if(listener != null)
                listener.errorIO();
        }
    }


    @Override
    public void gameStealResponse(DominoPiece dp) {
        mGame.getHandPieces().addPiece(dp);
        if(listener != null)
            listener.stealResponse(dp);
    }
    
    @Override
    public void errorResponse(ProtocolError e) {
        if(listener != null)
            listener.protocolErrorResponse(e);
    }

    @Override
    public void gameFinishedResponse(Winner winner, int score) {
        if(listener != null)
            listener.gameFinished(winner, score);
    }


    public interface OnServerResponseListener {
        public void initTiles(Pieces pieces, boolean clientStart);
        public void throwResponse(DominoPiece p, int restComp);
        public void stealResponse(DominoPiece p);
        public void protocolErrorResponse(ProtocolError e);
        public void gameFinished(Winner winner, int scoreComp); //Pasar por parámetro quien ha ganado
        public void errorIO();
    }
    
}
