/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view.views;

import controller.Controller;
import view.framework.View;
import controller.DominoGame;
import controller.connection.GameController;
import java.util.Scanner;
import model.StatMatch;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
import ub.swd.model.Pieces.Side;
import ub.swd.model.connection.AbstractProtocol;
import ub.swd.model.connection.ProtocolError;
import view.framework.ViewController;

/**
 *
 * @author zenbook
 */
public class PlayView extends View implements GameController.OnServerResponseListener{

    private DominoGame mGame;
    private GameController mGameController;
    private boolean finalGame;
    
    private DominoPiece tmpPiece;
    
    private String prevCommand;

    private static final String INV_COMMAND = "Invalid command. Put help (or ?) to show list of commands.";
    private static final String IDTILE_INVALID = "idtile must be an integer between 1 and size of hand.";
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
        prevCommand = null;
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
        showHelp();
        showBoard();
        while(!finalGame && mGame != null) {
            
            System.out.print("\n>> ");
            readCommand(sc);
        }
        
        return null;
    }
    
    
    
    private void readCommand(Scanner sc) {
        String[] cmdline = sc.nextLine().split(" ");
        if(cmdline.length == 0) {
            return;
        }
            
        String cmd = cmdline[0];
        String[] args = new String[cmdline.length-1];
        for (int i = 1; i < cmdline.length; i++) {
            args[i-1] = cmdline[i];
        }
        execCommand(cmd, args);
        
    }
    
    private void execCommand(String cmd, String[] args) {
        switch (cmd) {
            case "?":
            case "h":
            case "help":
                showHelp();
                break;
            case "bo":
            case "board":
                showBoard();
                break;
            case "th":
            case "throw":
                throwTile(args);
                break;
            case "re":
            case "reverse":
                reverse(args);
                break;
            case "st":
            case "steal":
                steal();
                break;
            case "hi":
            case "hint":
                hint();
                break;
            case "po":
            case "points":
                points();
                break;
            case "a":
            case "ant":
                previousCommand(args);
                break;
            case "exit":
                this.mGameController.close();
                finalGame = true;
                break;
            default:
                System.out.println(INV_COMMAND);
                break;
        }
        if(!(cmd.equals("a") || cmd.equals("ant")))
            prevCommand = cmd;
    }
    
    private void showHelp() {
        System.out.println("help                   (?,h)  --  Show this text");
        System.out.println("board                  (bo)   --  Show board and hand");
        System.out.println("throw <idtile> <R/L>   (th)   --  Throw a tile to the board. In first movement,"
                         + "\n                                  you can omit the second argument.");
        System.out.println("reverse <idtile>       (re)   --  Reverse tile");
        System.out.println("steal                  (st)   --  Steal a tile");
        System.out.println("hint                   (hi)   --  Show possible tiles that you can throw");
        System.out.println("points                 (po)   --  Show current points of client");
        System.out.println("ant <args>             (a)    --  Previous command used");
        System.out.println("exit                          --  Exit the game");
    }

    private void throwTile(String[] args) {
        int id;
        char s;
        if(!mGame.isFirstMovement()) {
            if(args.length != 2){
                System.out.println(INV_COMMAND);
                return;
            }
        }else{
            if(args.length != 2 && args.length !=1){
                System.out.println(INV_COMMAND);
                return;
            }
        }
        try {
            id = Integer.parseInt(args[0]);
        }catch(NumberFormatException ex) {
            System.out.println(IDTILE_INVALID);
            return;
        }
        
        if(id<=0 || id>mGame.getHandPieces().getNumPieces()) {
            System.out.println(IDTILE_INVALID);
            return;
        }
            
        
        if(args.length > 1) {
            s = args[1].charAt(0);
            if(args[1].length() > 1 || (s != 'R' && s != 'L' && s != 'l' && s != 'r')) {
                System.out.println("The side of movement must be 'R' or 'L'");
                return;
            }
        }else{
            s = 'L';
        }
        
        DominoPiece dp = mGame.getHandPieces().getPiece(id-1);
        Side side = (s == 'L' || s == 'l') ? Pieces.Side.LEFT : Pieces.Side.RIGHT;
        
        if(!mGame.canJoinToBoard(dp)) {
            System.out.println("You can't throw this tile");
            return;
        }
        
        if(!mGame.canPutTileOnBoard(dp, side)){
            dp.revert();
            if(!mGame.canPutTileOnBoard(dp, side)) {
                System.out.println("You can't throw this tile in this side");
                return;
            }
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
            id = Integer.parseInt(args[0]);
        }catch(NumberFormatException ex) {
            System.out.println(IDTILE_INVALID);
            return;
        }
        
        if(id<=0 || id>mGame.getHandPieces().getNumPieces()) {
            System.out.println(IDTILE_INVALID);
            return;
        }
        
        DominoPiece dp = mGame.getHandPieces().getPiece(id-1);
        dp.revert();
        
        showBoard();
    }

    private void steal() {
        if(!mGame.canSteal()) {
            System.out.println("You can't steal a tile");
            return;
        }
        mGameController.gameStealRequest();
    }
    
    private void hint() {
        Pieces pieces = mGame.getPossiblePiecesCanThrow();
        if(pieces.getNumPieces() == 0) {
            System.out.println("You must steal a tile");
            return;
        }
        System.out.println(pieces);
        for (int i = 0; i < mGame.getHandPieces().getNumPieces(); i++) {
            if(mGame.canJoinToBoard(mGame.getHandPieces().getPiece(i))) {
                System.out.print(" "+(i+1)+"    ");
            }
        }
        
    }
    
    private void points() {
        System.out.println("Your currrent points: "+mGame.getHandPieces().getScore());
    }
    
    private void previousCommand(String[] args) {
        if(prevCommand != null)
            execCommand(prevCommand, args);
        else{
            System.out.println("No previous command.");
        }
    }
    
    private void showBoard() {
        if(mGame != null) {
            System.out.println("\nBoard: "+mGame.getBoardPieces());
            System.out.println("Hand:  "+mGame.getHandPieces());
            System.out.print("       ");
            for (int i = 0; i < mGame.getHandPieces().getNumPieces(); i++) {
                System.out.print(" "+(i+1)+"    ");
            }
            System.out.println("");
        }
    }
    
    @Override
    public void stealResponse(DominoPiece p) {
        System.out.println("The tile stealed:\n");
        System.out.println(p+"\n");
    }

    @Override
    public void protocolErrorResponse(ProtocolError e) {
        System.out.println("\n"+e.type+": "+e.msg);
    }

    @Override
    public void initTiles(Pieces pieces, boolean clientStart) {
        if(clientStart)
            System.out.println("You start the game");
        else
            System.out.println("Computer did the first movement");
        System.out.println("You can start the game!\n");
    }

    @Override
    public void throwResponse(DominoPiece p, int restComp) {
        if(p == null) {
            System.out.println("The server couldn't throw.\n");
        }else{
            System.out.println("The server has thrown this tile:");
            System.out.println(p + "\n");
        }
        
        System.out.println("Remaining server tiles: "+restComp);
        showBoard();
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
        
        switch (winner) {
            case CLIENT:
                System.out.println("You win!");
                break;
            case SERVER:
                System.out.println("You're a loser!");
                break;
            case DRAW:
                System.out.println("Draw! Points: "+scoreComp);
                break;
            default:
                System.out.println("Not defined.");
        }
        
        /*Save stats game*/
        
        Controller c = parent.getController();
        StatMatch stat = new StatMatch(winner, c.getIp());
        c.getStats().addStat(stat);
        
        /*Change flag of game*/
        finalGame = true;
    }
    

}
