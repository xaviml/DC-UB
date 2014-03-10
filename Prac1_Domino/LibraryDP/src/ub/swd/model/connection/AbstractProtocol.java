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
import ub.swd.model.connection.ProtocolError.ErrorType;

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
        byte b = comUtils.readByte();
        DominoPiece dp;
        Side dps;
        switch (b){
            /* ERROR FRAME */
            case 0x00:
                // Server cannot recieve this message ever.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(new ProtocolError(ErrorType.SYNTAX_ERR,"Invalid frame ID"));
                    return;
                }
                /* Read the err */
                ErrorType errorType = readErrorType();
                String s = comUtils.readStringVariable(3);
                
                errorResponse(new ProtocolError(errorType,s));
                
                break;
                
            /* HELLO FRAME */
            case 0x01:
                // Client might not recieve this.
                // No frame to read.
                if (side == ProtocolSide.CLIENT_SIDE) {
                    return;
                }
                helloFrameRequest();
                break;
                
            /* HELLO-RESPONSE FRAME */
            case 0x02:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(new ProtocolError(ErrorType.SYNTAX_ERR,"Invalid frame ID"));
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
                
                if (side == ProtocolSide.CLIENT_SIDE) {
                    return;
                }
                
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
                    errorResponse(new ProtocolError(ErrorType.SYNTAX_ERR,"Invalid frame ID"));
                    return;
                }
                
                dp = readDominoPiece();
                dps = readSide();
                int rest = comUtils.readInt32();
                
                gamePlayResponse(dp, dps, rest);
                
                break;
                
            /* STEAL-RESPONSE FRAME */
            case 0x05:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(new ProtocolError(ErrorType.SYNTAX_ERR,"Invalid frame ID"));
                    return;
                }
                
                dp = readDominoPiece();
                gameStealResponse(dp);
                
                break;
            
            /* FINAL FRAME */
            case 0x06:
                // Server might not recieve this frame.
                if (side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(new ProtocolError(ErrorType.SYNTAX_ERR,"Invalid frame ID"));
                    return;
                }
                
                Winner winner = readWinner();
                int score = -1;
                if(winner == Winner.DRAW) {
                    score = comUtils.readInt32();
                }
                
                gameFinishedResponse(winner, score);
                break;
            default:
                if(side == ProtocolSide.SERVER_SIDE) {
                    errorResponse(new ProtocolError(ErrorType.SYNTAX_ERR, "Invalid frame ID"));
                }
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
        return new DominoPiece(left-48, right-48);
    }
    
    public void writeNoMovementFrame() throws IOException {
        comUtils.writeString("NT ");
    }
    
    public void writeDominoPiece(DominoPiece p) throws IOException{
        if(p == null)
            comUtils.writeString("NT");
        else{
            comUtils.writeChar((char)(p.getLeftNumber()+48)); //int to ascii
            comUtils.writeChar((char) (p.getRightNumber()+48));
        }
    }
    
    public Side readSide() throws IOException {
        char c = comUtils.readChar();
        switch (c) {
            case 'R':
                return Side.RIGHT;
            case 'L':
                return Side.LEFT;
            case ' ':
                return null;
            default:
                throw new AssertionError();
        }
    }
    
    public void writeSide(Side s) throws IOException {
        comUtils.writeChar((s == Side.LEFT) ? 'L' : 'R');
    }
    
    public Winner readWinner() throws IOException {
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
    
    public void writeWinner(Winner w) throws IOException {
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
    
    public ErrorType readErrorType() throws IOException {
        byte type = (byte)comUtils.readByte();
        System.out.println(type);
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
    public abstract void errorResponse(ProtocolError e);
    
    //--------------------------------------------------------------------------
}
