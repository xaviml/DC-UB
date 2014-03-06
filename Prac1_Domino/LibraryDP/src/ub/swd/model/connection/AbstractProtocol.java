/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.swd.model.connection;

import java.io.IOException;
import java.net.Socket;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;
import ub.swd.model.connection.Error.ErrorType;

/**
 * AbstracProtocol. This abstract class will transform all messages into readable
 * objects. All protocols (Client/Server) will extend this class.
 * @author Pablo
 */
public abstract class AbstractProtocol {
    
    public enum ProtocolSide{SERVER_SIDE, CLIENT_SIDE};
    
    protected ProtocolSide side;
    protected Socket socket;
    protected ComUtils comUtils;
    
    public AbstractProtocol(Socket socket, ProtocolSide side) throws IOException{
        this.comUtils = new ComUtils(socket);
        this.socket = socket;
        this.side = side;
    }
    
    public Socket getSocket() { return socket; }
    
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            
        }
    }
    
    /**
     * This function will be called instead of "socket.read()". here we will
     * determinate the frame we are on and do an action.
     * example: If server reads the frame num 2, will return a protocolError.
     * @throws java.io.IOException 
     */
    public void readFrame() throws IOException{
        byte b = comUtils.read_bytes(1)[0];
        switch (b){
            /* ERROR FRAME */
            case 0x00:
                // Server cannot recieve this message ever.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                    return;
                }
                /* Read the err */
                int type = comUtils.read_int32();
                String s = comUtils.read_string_variable(140);
                
                // TODO: Select error
                errorResponse(ErrorType.SYNTAX_ERR, s);
                
                
                break;
                
            /* HELLO FRAME */
            case 0x01:
                // Client might not recieve this.
                // No frame to read.
                helloFrameRequest();
                break;
                
            /* HELLO-RESPONSE FRAME */
            case 0x02:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                //--
                break;
                
            /* TURN FRAME */
            case 0x03:
                //--
                
                // If the read contains a play -> gamePlayRequest(..)
                
                // If the read doesn't contain a play -> gameStealRequest()
                
                
                break;
                
            /* TURN-RESPONSE FRAME */
            case 0x04:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                //--
                break;
                
            /* STEAL-RESPONSE FRAME */
            case 0x05:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                //--
                break;
            
            /* FINAL FRAME */
            case 0x06:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                //--
                break;
            default:
                //-- PROTOCOL ERROR
                break;
        }
                    
    }
    //--------------------------------------------------------------------------
    // R/W Object functions
    public Pieces readPieces(){
        // TODO
        return null;
    }
    public void writePieces(Pieces p){
        // TODO
    }
    public DominoPiece readDominoPiece(DominoPiece p){
        // TODO
        return null;
    }
    public void writeDominoPiece(DominoPiece p){
        // TODO
    }
    //--------------------------------------------------------------------------
    
    
    //--------------------------------------------------------------------------
    //  Request/Response abstract functions, to be implemented by client and server
    
    
    // Client request
    public abstract void helloFrameRequest();                       // No parameters
    public abstract void gamePlayRequest(DominoPiece p, Side s);
    public abstract void gameStealRequest();
    
    // Server response
    public abstract void helloFrameResponse(Pieces hand, DominoPiece compTurn, Side s);
    public abstract void gamePlayResponse(DominoPiece p, Side s);
    public abstract void gameStealResponse(DominoPiece dp);
    public abstract void gameFinishedResponse(int sc1, int piecesLeft);
    public abstract void errorResponse(ErrorType e, String s);
    
    //--------------------------------------------------------------------------
}
