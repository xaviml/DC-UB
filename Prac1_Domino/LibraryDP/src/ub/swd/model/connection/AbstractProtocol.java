/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.swd.model.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public enum Winner{SERVER, CLIENT, DRAW};
    
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
     * 
     * @throws java.io.IOException 
     */
    public void readFrame() throws IOException{
        byte b = comUtils.read_bytes(1)[0];
        DominoPiece dp;
        Side dps;
        switch (b){
            /* ERROR FRAME */
            case 0x00:
                // Server cannot recieve this message ever.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                    return;
                }
                /* Read the err */
                ErrorType errorType = readErrorType();
                String s = comUtils.read_string_variable(3);
                
                errorResponse(errorType, s);
                
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
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                    return;
                }
                
                Pieces pieces = readPieces();
                dp = readDominoPiece();
                if(dp == null) {
                    //El cliente coloca la primera prieza
                }else{
                    //El servidor ha colocado la primera pieza
                }
                
                helloFrameResponse(pieces, dp);
                break;
                
            /* TURN FRAME */
            case 0x03:
                
                dp = readDominoPiece();
                dps = readSide();
                
                if(dp == null) {        // If the read doesn't contain a play -> gameStealRequest()
                    this.gameStealRequest();
                } else {                // If the read contains a play -> gamePlayRequest(..)
                    this.gamePlayRequest(dp, dps);
                }
                
                break;
                
            /* TURN-RESPONSE FRAME */
            case 0x04:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                    return;
                }
                
                dp = readDominoPiece();
                dps = readSide();
                int rest = comUtils.read_int32();
                
                gamePlayResponse(dp, dps, rest);
                
                break;
                
            /* STEAL-RESPONSE FRAME */
            case 0x05:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                    return;
                }
                
                dp = readDominoPiece();
                gameStealResponse(dp);
                
                break;
            
            /* FINAL FRAME */
            case 0x06:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(ErrorType.SYNTAX_ERR,"Invalid frame ID");
                    return;
                }
                
                Winner winner = readWinner();
                int score = -1;
                if(winner == Winner.DRAW) {
                    score = comUtils.read_int32();
                }
                
                gameFinishedResponse(winner, score);
                break;
            default:
                //-- PROTOCOL ERROR
                break;
        }
                    
    }
    //--------------------------------------------------------------------------
    // R/W Object functions
    public Pieces readPieces() throws IOException{
        Pieces pieces = new Pieces(Pieces.ListType.UNSORTED);
        for (int i = 0; i < 7; i++)
            pieces.addPiece(readDominoPiece());
        return pieces;
    }
    public void writePieces(Pieces p) throws IOException{
        for (DominoPiece dominoPiece : p) {
            writeDominoPiece(dominoPiece);
        }
    }
    public DominoPiece readDominoPiece() throws IOException{
        char left = comUtils.readChar();
        char right  = comUtils.readChar();
        if(left == 'N' && right == 'T')
            return null;
        return new DominoPiece(left, right);
    }
    public void writeDominoPiece(DominoPiece p) throws IOException{
        if(p == null)
            //CUIDADO!
            comUtils.write_string("NT");
        comUtils.writeChar((char)(p.getLeftNumber()+30)); //int to ascii
        comUtils.writeChar((char) (p.getRightNumber()+30));
    }
    
    public Side readSide() throws IOException {
        //ESPACIO del: "NT "
        return (comUtils.readChar() == 'R') ? Side.RIGHT : Side.LEFT;
    }
    
    public void writeSide(Side s) throws IOException {
        comUtils.writeChar((s == Side.LEFT) ? 'L' : 'R');
    }
    
    private Winner readWinner() throws IOException {
        switch (comUtils.readByte()) {
            case 0x00:
                return Winner.CLIENT;
            case 0x01:
                return Winner.SERVER;
            case 0x02:
                return Winner.DRAW;
            default:
                throw new AssertionError();
        }
    }
    
    private void writeWinner(Winner w) throws IOException {
        switch (w) {
            case CLIENT:
                comUtils.writeByte((byte)0x00);
                break;
            case SERVER:
                comUtils.writeByte((byte)0x01);
                break;
            case DRAW:
                comUtils.writeByte((byte)0x02);
                break;
            default:
                throw new AssertionError();
        }
        
    }
    
    private ErrorType readErrorType() throws IOException {
        byte type = comUtils.read_bytes(1)[0];
        switch (type) {
                    case 0x00:
                        return ErrorType.SYNTAX_ERR;
                    case 0x01:
                        return ErrorType.ILLEGAL_ACTION_ERR;
                    case 0x02:
                        return ErrorType.NOT_ENOUGH_RESOURCES_ERR;
                    case 0x03:
                        return ErrorType.INTERNAL_SERVER_ERR;
                    case 0x04:
                        return ErrorType.UNDEFINED_ERR;
                    default:
                        throw new AssertionError();
                        //TODO: Controlar
                }
    }
    
    public void writeErrorType(ErrorType errorType) throws IOException {
         switch (errorType  ) {
            case SYNTAX_ERR:
                comUtils.writeByte((byte) 0x00);
                break;
            case ILLEGAL_ACTION_ERR:
                comUtils.writeByte((byte) 0x01);
                break;
            case NOT_ENOUGH_RESOURCES_ERR:
                comUtils.writeByte((byte) 0x02);
                break;
            case INTERNAL_SERVER_ERR:
                comUtils.writeByte((byte) 0x03);
                break;
            case UNDEFINED_ERR:
                comUtils.writeByte((byte) 0x04);
                break;
            default:
                throw new AssertionError();
                //TODO: Controlar
        }
    }
    //--------------------------------------------------------------------------
    
    
    //--------------------------------------------------------------------------
    //  Request/Response abstract functions, to be implemented by client and server
    
    
    // Client request
    public abstract void helloFrameRequest();                       // No parameters
    public abstract void gamePlayRequest(DominoPiece p, Side s);
    public abstract void gameStealRequest();
    
    // Server response
    public abstract void helloFrameResponse(Pieces hand, DominoPiece compTurn);
    public abstract void gamePlayResponse(DominoPiece p, Side s, int rest);
    public abstract void gameStealResponse(DominoPiece dp);
    public abstract void gameFinishedResponse(Winner winner, int score);
    public abstract void errorResponse(ErrorType e, String s);
    
    //--------------------------------------------------------------------------
}
