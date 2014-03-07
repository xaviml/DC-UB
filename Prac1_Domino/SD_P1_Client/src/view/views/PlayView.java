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
import javax.management.MBeanConstructorInfo;
import model.StatMatch;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;
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
    
    private DominoPiece tmpPiece;

    private static final String INV_COMMAND = "Invalid command. Put help to show list of commands";
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
        
        //Put a msg tutorial
        //possible functions:
        //help -- show this hint
        //throw <idpiece> -- Throw piece
        //reverse <idpiece> -- Revert piece
        //steal -- steal a piece
        //exit
        
        while(!finalGame) {
            seeBoard();
            System.out.print("\n>> ");
            readCommand(sc);
        }
        
        
        /*
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
        */
        return null;
    }
    
    
    
    private void readCommand(Scanner sc) {
        String[] cmdline = sc.next().split(" ");
        String cmd = cmdline[0];
        String[] args = new String[cmdline.length-1];
        for (int i = 1; i < cmdline.length; i++) {
            args[i-1] = cmdline[i];
        }
        
        switch (cmd) {
            case "help":
                showHelp();
                break;
            case "throw":
                throwTile(args);
                break;
            case "reverse":
                reverse(args);
                break;
            case "steal":
                steal();
                break;
            case "hint":
                hint();
                break;
            case "points":
                points();
                break;
            case "exit":
                this.mGameController.close();
                break;
            default:
                System.out.println(INV_COMMAND);
                break;
        }

    }
    
    private void showHelp() {
        //help -- show this hint
        //throw <idpiece> -- Throw piece
        //reverse <idpiece> -- Revert piece
        //steal -- steal a piece
        //hint
        //points
        //exit
        System.out.println("help                  --  Show this text");
        System.out.println("throw <idtile> <R/L>  --  Throw a tile to the board");
        System.out.println("reverse <idtile>      --  Reverse tile");
        System.out.println("steal                 --  Steal a tile");
        System.out.println("hint                  --  Show possible tiles that you can throw it");
        System.out.println("points                --  Show current points of client");
    }

    private void throwTile(String[] args) {
        int id;
        char s;
        
        if(args.length != 2){
            System.out.println(INV_COMMAND);
            return;
        }
        try {
            id = Integer.parseInt(args[1]);
        }catch(InputMismatchException ex) {
            System.out.println(INV_COMMAND);
            return;
        }
        s = args[1].charAt(0);
        if(args[1].length() > 1 || s != 'R' || s != 'L'|| s != 'l' || s != 'r') {
            System.out.println(INV_COMMAND);
            return;
        }
        
        DominoPiece dp = mGame.getHandPieces().getPiece(id-1);
        Side side = (s == 'L' || s == 'l') ? Pieces.Side.LEFT : Pieces.Side.RIGHT;
        
        if(!mGame.canJoinToBoard(dp)) {
            System.out.println("You can't throw this tile");
            return;
        }
        
        
        
        this.mGameController.gamePlayRequest(dp, side);
            
    }

    private void reverse(String[] args) {
        int id;
        if(args.length != 1){
            System.out.println(INV_COMMAND);
            return;
        }
        try {
            id = Integer.parseInt(args[1]);
        }catch(InputMismatchException ex) {
            System.out.println(INV_COMMAND);
            return;
        }
        
        DominoPiece dp = mGame.getHandPieces().getPiece(id-1);
        dp.revert();
    }

    private void steal() {
        if(!mGame.canSteal()) {
            System.out.println("You can't steal a tile");
            return;
        }
        mGameController.gameStealRequest();
    }
    
    private void hint() {
        System.out.println(mGame.getPossiblePiecesCanThrow());
        for (int i = 0; i < mGame.getHandPieces().getNumPieces(); i++) {
            if(mGame.canJoinToBoard(mGame.getHandPieces().getPiece(i)))
                System.out.print(" "+(i+1)+"    ");
        }
    }
    
    private void points() {
        System.out.println("Your currrent points: "+mGame.getHandPieces().getScore());
    }
    
    private void seeBoard() {
        System.out.println("Board: "+mGame.getBoardPieces()+"\n");
        System.out.println("Hand:  "+mGame.getHandPieces());
        System.out.print("       ");
        for (int i = 0; i < mGame.getHandPieces().getNumPieces(); i++) {
            System.out.print(" "+(i+1)+"    ");
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
        if(clientStart)
            System.out.println("You start the game");
        else
            System.out.println("Computer did the first movement");
        System.out.println("You can start the game! Press 'To watch board' on menu to watch yours tiles.\n");
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
