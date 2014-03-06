/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.game;

import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;

/**
 * This is the main class for the game, will recive petitions from gameController
 * and manage all the data. It will also be responsible of the data missMatch errors
 * For example, connection sending a Piece that it doesn't have.
 * 
 * This class will also perform as game's IA.
 * @author Pablo
 */
public class Game {
    public enum GameState{STARTING, PLAYER_TURN, COMP_TURN, FINISHED};
    private Pieces resto;
    private Pieces compHand;
    private Pieces playerHand;
    private Pieces game;
    private GameState gameState;
    
        
    public Game(){
        
        this.gameState = GameState.STARTING;
        resto = new Pieces(Pieces.ListType.UNSORTED);
        playerHand = new Pieces(Pieces.ListType.UNSORTED);
        compHand = new Pieces(Pieces.ListType.UNSORTED);
        game = new Pieces(Pieces.ListType.SORTED);
        
    }

    public void endGame(){
        this.gameState = GameState.FINISHED;
    }
    /**
     * initGame(). This method is used to initialize server/client hand.
     * @return Pieces on the client hand.
     */
    public Pieces initGame(){
        // First start the resto with all domino pieces
        initResto();
        
        
        DominoPiece compBest = null;
        DominoPiece playerBest = null;
        
        DominoPiece dp;
        // Give 7 Pieces to the comp, and 7 to the player.
        for (int i = 0; i < 7; i++) {
            // While we get pieces we calculate the first turn.
            dp = resto.takeRandomPiece();
            compBest = (dp.isThisBetter(compBest))? compBest: dp;
            compHand.addPiece(dp);
            
            dp = resto.takeRandomPiece();
            playerBest = (dp.isThisBetter(playerBest))? playerBest: dp;
            playerHand.addPiece(dp);
            
        }
        
        // Set the starting turn.
        if (compBest.isThisBetter(playerBest)){ //<- Nah, this will never be null
            this.gameState = GameState.PLAYER_TURN;
        } else{
            this.gameState = GameState.COMP_TURN;
        }
        
        // Return his hand
        return playerHand;
    }
    
    
    /**
     * Initialize all Domino pieces
     */
    private void initResto(){
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                resto.addPiece(new DominoPiece(i, j));
            }
        }
    }

    

    public DominoPiece steal() {
        // Check that resto isn't empty
        if (resto.getNumPieces()== 0){
            this.gameState = GameState.COMP_TURN;     //Toggle turn
            return null;
        }
        /* if isn't empty steal a piece */
        DominoPiece stealed = resto.takeRandomPiece();
        playerHand.addPiece(stealed);
        
        return stealed;
    }

    public boolean throwing(DominoPiece piece, Pieces.Side side) {
        /* Check if the piece is owned by the client */
        if (!playerHand.contains(piece)){
            return false;
        }
        
        /* Check if the movement is possible */
        boolean flag = game.addPiece(piece, side);
        
        /* Check if the piece fits. */
        if (!flag){
            return false;
        }
        
        /* If everything went OK */
        playerHand.removePiece(piece);      
        return true;
    }

    public int getPlayerScore() {
        if (playerHand.getNumPieces() == 0) return -1;
        int score = 0;
        for(Object o: playerHand){
            DominoPiece dp = (DominoPiece) o;
            score += dp.getLeftNumber()+dp.getRightNumber();
        }
        return score;
    }

    public int getComputerScore() {
        if (compHand.getNumPieces() == 0) return -1;
        int score = 0;
        for(Object o: compHand){
            DominoPiece dp = (DominoPiece) o;
            score += dp.getLeftNumber()+dp.getRightNumber();
        }
        return score;
    }
    
    /**
     * IA. This is the method that takes on consideration the turn of the server.
     * @return 
     */
     public Object[] computerTurn() {
        Object [] o = new Object[2];                            // [Piece, Side] -0 left, 1 right-
        /* Check if computer can set a piece in the table */
        for (Object obj: compHand){
            DominoPiece dp = (DominoPiece) obj;
            if (game.addPiece(dp, Pieces.Side.LEFT)){
                compHand.removePiece(dp);
                o[0] = dp;
                o[1] = Pieces.Side.LEFT;
                System.out.println("SYSTEM THROWS "+dp+" on "+0);
                this.gameState = GameState.PLAYER_TURN;     //Toggle turn
                return o;
            }
            else if(game.addPiece(dp, Pieces.Side.RIGHT)){
                compHand.removePiece(dp);
                o[0] = dp;
                o[1] = Pieces.Side.RIGHT;
                System.out.println("SYSTEM THROWS ");
                this.gameState = GameState.PLAYER_TURN;     //Toggle turn
                return o;
            }
            
        }
        /* Computer can't play, let's steal a piece */
        
        // Check that resto isn't empty
        if (resto.getNumPieces()== 0){
            System.out.println("SYSTEM CANT STEAL! NO RESTO");
            this.gameState = GameState.PLAYER_TURN;     //Toggle turn
            return new Object[]{null,null};
        }
        // Steal a piece
        DominoPiece stealed = resto.takeRandomPiece();
        compHand.addPiece(stealed);
        System.out.println("SYSTEM STEALS " +stealed);
        
        // Another call to the function until server will be able to move, or
        // No pieces left in the resto
        return computerTurn();
    }
     
     public boolean isPlayerTurn(){
         return (gameState == GameState.PLAYER_TURN);
     }
     
     public boolean isComputerTurn(){
         return (gameState == GameState.COMP_TURN);
     }
     
     public boolean isGameOver(){
         return (gameState == GameState.FINISHED);
     }
     
     public int getNumComputerPieces() { return this.compHand.getNumPieces(); }

}
