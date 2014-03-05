/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model.connection;

import ub.swd.model.connection.ComUtils;
import java.io.IOException;
import ub.swd.model.connection.Turn;

/**
 * Protocol. This class will be responsible to get all messages from the socket
 * and transform them into functions. We are including here a *Connection*
 * @author Pablo
 */
public class Protocol /*extends AbstractProtocol*/{
    private Connection connection;
    private ComUtils com;
    
    public Protocol (Connection c) throws IOException{
        //super(c.getSocket());
        this.connection = c;
    }

}
