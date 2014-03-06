/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller.connection;

import controller.DominoGame;
import java.io.IOException;
import java.net.Socket;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.connection.AbstractProtocol;
import ub.swd.model.connection.Error.ErrorType;
import ub.swd.model.connection.Error;

/**
 *
 * @author Xavi Moreno
 */
public class GameController extends AbstractProtocol{
    
    private DominoGame mGame;
    private OnServerResponseListener listener;

    public GameController(Socket socket, ProtocolSide side) throws IOException {
        super(socket, side);
    }
    
    public void setOnServerResponseListener(OnServerResponseListener l) {
        this.listener = l;
    }

    @Override
    public void helloFrameRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gamePlayRequest(DominoPiece p, Pieces.Side s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void errorResponse(ErrorType e, String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameStealRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void helloFrameResponse(Pieces hand, DominoPiece compTurn, Pieces.Side s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gamePlayResponse(DominoPiece p, Pieces.Side s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameStealResponse(DominoPiece dp) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameFinishedResponse(int sc1, int piecesLeft) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public interface OnServerResponseListener {
        public void throwResponse(DominoPiece p);
        public void stealResponse(DominoPiece p);
        public void errorResponse(Error e);
        public void gameFinished(); //Pasar por parámetro quien ha ganado
    }
    
}
