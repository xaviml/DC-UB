/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model.connection;

import java.io.IOException;
import java.net.Socket;
import model.game.Turn;

/**
 *
 * @author Pablo
 */
public abstract class AbstractProtocol {
    private Socket socket;
    private ComUtils comUtils;
    public AbstractProtocol(Socket s) throws IOException{
        this.comUtils = new ComUtils(socket);
        this.socket = s;
    }
    
    public void readFrameNumber() throws IOException{
        comUtils.read_int32();
    }
    
    public void executeFrame(int i){
        switch (i){
            case 0:
                //Error message here
                errorResponse(null);
                break;
            case 1:
                //Hello Frame
                break;
            case 2:
                //--
                break;
            case 3:
                //--
                break;
            case 4:
                //--
                break;
        }
                    
    }
    // Client request (Server must implement these)
    public abstract void helloFrameRequest(Turn t);
    public abstract void gamePlayRequest(Turn t);
    public abstract void gameStealRequest(Turn t);
    
    // Server response (Client must implement these)
    public abstract void helloFrameResponse(Turn t);
    public abstract void gamePlayResponse(Turn t);
    public abstract void gameStealResponse(Turn t);
    public abstract void gameFinishedResponse(Turn t);
    public abstract void errorResponse(Turn t);
}
