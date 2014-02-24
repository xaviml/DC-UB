/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

/**
 * This is the main class for the game, will recive petitions from gameController
 * and manage all the data. It will also be responsible of the data missMatch errors
 * For example, connection sending a Piece that it doesn't have.
 * 
 * This class will also perform as game's IA.
 * @author Pablo
 */
public class Game {
    private Pieces resto;
    private Pieces compHand;
    private Pieces playerHand;
    private Pieces game;
    
    public Game(){
        // First create all domino pieces in "resto" list
        resto = new Pieces(Pieces.ListType.UNSORTED);
        playerHand = new Pieces(Pieces.ListType.UNSORTED);
        compHand = new Pieces(Pieces.ListType.UNSORTED);

    }

    protected Turn initGame(){
        // First start the resto with all domino pieces
        initResto();
        
        // Give 7 Pieces to the comp, and 7 to the player.
        DominoPiece compBest = null;
        DominoPiece playerBest = null;
        
        DominoPiece dp;
        
        for (int i = 0; i < 7; i++) {
            // While we get pieces we calculate the first turn.
            dp = resto.takeRandomPiece();
            compBest = (dp.isThisBetter(compBest))? compBest: dp;
            compHand.addPiece(dp);
            
            dp = resto.takeRandomPiece();
            playerBest = (dp.isThisBetter(playerBest))? playerBest: dp;
            playerHand.addPiece(dp);
            
        }
        
        Turn t = new Turn();
        if (compBest.isThisBetter(playerBest)){
            // Player Starts
            // Retrive it's basic info
            t.playerHand = playerHand.getList();
            return t;
            
            
        } else{
            // Computer Starts
            t = computerTurn(null);
            t.playerHand = playerHand.getList();
            return t;
        }
        
        /** =========== Test stuff
        for (DominoPiece d : compHand.viewPieces()){
            System.out.println(d);
        }
        System.out.println("==============================");
        for (DominoPiece d : playerHand.viewPieces()){
            System.out.println(d);
        }
        */
    }
    
    
    /**
     * Initialize all Domino pieces
     */
    protected void initResto(){
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                resto.addPiece(new DominoPiece(i, j));
            }
        }
    }

    protected Turn computerTurn(Turn t) {
        if (t == null){
            t = new Turn();
        }

        /* Check if computer can set a piece in the table */
        for (Object obj: compHand){
            DominoPiece dp = (DominoPiece) obj;
            if (game.addPiece(dp, Pieces.Side.LEFT)){
                compHand.removePiece(dp);
                t.side = Pieces.Side.LEFT;
                t.serverPiece = dp;
                t.serverPiecesAmmount = compHand.getSize();
                t.gameEndFlag = (compHand.getSize() == 0);
                return t;
            }
            else if(game.addPiece(dp, Pieces.Side.RIGHT)){
                compHand.removePiece(dp);
                t.side = Pieces.Side.LEFT;
                t.serverPiece = dp;
                t.serverPiecesAmmount = compHand.getSize();
                t.gameEndFlag = (compHand.getSize() == 0);
                return t;
            }
        }
        /* Computer can't play, let's steal a piece */
        
        // Check that resto isn't empty
        if (resto.getSize() == 0){
            t.computerCantPlayFlag = true;
            return t;
        }
        // Steal a piece
        DominoPiece stealed = resto.takeRandomPiece();
        compHand.addPiece(stealed);
        t.serverPiecesAmmount = compHand.getSize();
        return t;
    }

    protected Turn steal() {
        // Check that resto isn't empty
        Turn t = new Turn();
        if (resto.getSize() == 0){
            t.playerCantPlayFlag = true;
        }
        else{
            /* if isn't empty steal a piece */
            DominoPiece stealed = resto.takeRandomPiece();
            playerHand.addPiece(stealed);
            t.pieceStealed = stealed;
        }
        
        // Now it's turn for the computer.
        t = computerTurn(t);
        return t;
    }

    protected Turn throwing(DominoPiece piece, Pieces.Side side) {
        Turn t = new Turn();
        /* Check if the piece is owned by the client */
        if (!playerHand.contains(piece)){
            t.missMatchFlag = true;
            return t;
        }
        
        /* Check if the movement is possible */
        boolean flag = game.addPiece(piece, side);
        
        /* Manage the error */
        if (!flag){
            t.missMatchFlag = true;
            return t;
        }
        
        /* If everything went OK */
        playerHand.removePiece(piece);      
        /* Manage player win */
        if (playerHand.getSize() == 0){
            t.gameEndFlag = true;
            return t;
        }
        // If game haven't finished yet let computer play;
        t = computerTurn(t);
        
        return t;
    }
    
}
