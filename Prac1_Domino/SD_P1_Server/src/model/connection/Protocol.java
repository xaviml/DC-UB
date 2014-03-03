/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model.connection;

import java.io.IOException;
import model.game.Turn;

/**
 * Protocol. This class will be responsible to get all messages from the socket
 * and transform them into functions. We are including here a *Connection*
 * @author Pablo
 */
public class Protocol extends AbstractProtocol{
    private Connection connection;
    private ComUtils com;
    
    public Protocol (Connection c) throws IOException{
        super(c.getSocket());
        this.connection = c;
    }

    @Override
    public void helloFrameRequest(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gamePlayRequest(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameStealRequest(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void helloFrameResponse(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gamePlayResponse(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameStealResponse(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gameFinishedResponse(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void errorResponse(Turn t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}
