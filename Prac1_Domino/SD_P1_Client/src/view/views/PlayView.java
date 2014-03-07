/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import controller.Controller;
import view.framework.View;
import controller.DominoGame;
import controller.connection.GameController;
import java.util.InputMismatchException;
import java.util.Scanner;
import model.StatMatch;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.connection.AbstractProtocol;
import ub.swd.model.connection.ProtocolError;
import view.framework.ViewController;
import view.menu.Menu;

/**
 *
 * @author zenbook
 */
public class PlayView extends View implements GameController.OnServerResponseListener{

    private DominoGame mGame;
    private GameController mGameController;
    private boolean finalGame;

    
    private static final String MSG_ERROR_IO = "It hasn't been able to establish a connection to the server.\n"
                + "\tCheck the IP adress and port.\n"
                + "\tCheck the network connection on your computer.\n"
                + "\tIf your computer or network is protected by a firewall or proxy, make sure that this application has the permissions enabled.";
    
    private static enum OpcionsPlayMenu {
        SEE_BOARD, SEE_LEFT_AND_RIGHT, SEE_HAND, STEAL, SORTIR
    };
    
    private static final String[] descPlayMenu = {
        "To watch board",
        "To watch right and left numbers of the board",
        "To throw a tile",
        "To steal a tile",
        "Exit of this match"
    };
    
    public PlayView(ViewController parent) {
        super(parent);
        mGameController = parent.getController().createGame();
        finalGame=false;
    }

    @Override
    public String getTitle() {
        return "Play";
    }

    @Override
    public Class run(Scanner sc) {
        if(mGameController == null) {
            System.out.println(MSG_ERROR_IO);
            Controller c = parent.getController();
            c.getStats().addProblemIP(c.getIp());
            return null;
        } else{
            mGameController.setOnServerResponseListener(this);
            mGameController.helloFrameRequest();
            mGame = mGameController.getGame();
        }
        Menu<OpcionsPlayMenu> menu;
        menu = new Menu(OpcionsPlayMenu.values(), descPlayMenu);
        OpcionsPlayMenu op = null;

        while (op != OpcionsPlayMenu.SORTIR && !finalGame) {
            menu.mostrarMenu();
            op = menu.getOpcio(sc);
            
            switch (op) {
                case SEE_BOARD:
                    seeBoard();
                    break;
                case SEE_LEFT_AND_RIGHT:
                    seeLeftRightPieces();
                    break;
                case SEE_HAND:
                    seeHand(sc);
                    break;
                case STEAL:
                    stealTile();
                    break;
                case SORTIR:
                    this.mGameController.close();
                    break;
            }
        } 
        return null;
    }
    
    private void seeBoard() {
        System.out.println(mGame.getBoardPieces()+"\n");
        seeLeftRightPieces();
    }
    
    private void seeLeftRightPieces() {
        System.out.println("Left: "+mGame.getBoardPieces().getLeftSide());
        System.out.println("Right: "+mGame.getBoardPieces().getRightSide());
    }
    
    private void stealTile() {
        if(mGame.canSteal())
            mGameController.gameStealRequest();
        else
            System.out.println("You're forced to throw a tile");
    }
    
    private void seeHand(Scanner sc) {
        Pieces pieces = mGame.getHandPieces();
        for (int i = 0; i < pieces.getNumPieces(); i++) {
            System.out.println(i+1);
            System.out.println(pieces.getPiece(i));
            System.out.println("");
        }
        
        int op;
        System.out.println("-1 if you want to show only the tiles that you can throw");
        System.out.println("0 if you want back to main menu");
        do {
            System.out.print("\nPut the identifier to choose a tile: ");
            try {
                op = sc.nextInt();
            }catch(InputMismatchException ex) {
                op = -2;
            }
        } while (op < -1 || op > pieces.getNumPieces());
        
        switch (op) {
            case -1:
                System.out.println(mGame.getPossiblePiecesCanThrow());
                break;
            default:
                String side;
                do{
                    System.out.println("Choose side (L/R): ");
                    side = sc.next();
                }while(!side.equals("L") || !side.equals("l") || !side.equals("R") || !side.equals("r"));
                DominoPiece piecesSelected = mGame.getHandPieces().getPiece(op-1);
                mGameController.gamePlayRequest(piecesSelected, (side.equals("L") || side.equals("l")) ? Pieces.Side.LEFT : Pieces.Side.RIGHT);
                break;
        }
    }
    
    @Override
    public void stealResponse(DominoPiece p) {
        System.out.println("The tile stealed is this.\n");
        System.out.println(p);
    }

    @Override
    public void protocolErrorResponse(ProtocolError e) {
        System.err.println(e.msg);
    }

    @Override
    public void initTiles(Pieces pieces, boolean clientStart) {
        System.out.println("You can start the game! Press 'See board' on menu to watch yours tiles.");
    }

    @Override
    public void throwResponse(DominoPiece p, int restComp) {
        System.out.println("The server throw this piece: ");
        System.out.println(p + "\n");
        System.out.println("Remaining server tiles: "+restComp);
    }

    @Override
    public void errorIO() {
        System.out.println(MSG_ERROR_IO);
        Controller c = parent.getController();
        c.getStats().addProblemIP(c.getIp());
        finalGame = true;
    }
    
    @Override
    public void gameFinished(AbstractProtocol.Winner winner, int scoreComp) {
        Controller c = parent.getController();
        StatMatch stat = new StatMatch(winner, c.getIp());
        c.getStats().addStat(stat);
        finalGame = true;
    }
}
