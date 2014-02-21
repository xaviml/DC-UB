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

    public Turn initGame(){
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
        
        if (compBest.isThisBetter(playerBest)){
            // Player Starts
        } else{
            // Computer Starts
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
        return null;
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
    
}
