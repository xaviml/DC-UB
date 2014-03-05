/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.swd.model.connection;

import java.util.ArrayList;
import ub.swd.model.DominoPiece;
import ub.swd.model.Pieces;
/**
 * After the play of the client, we will always return a Turn object, which
 * contains all the changes made in the game in order to ensure the coherence
 * of the information.
 * 
 * The variables in this class might be not initialized depending on the turn.
 * 
 * @author Pablo
 */
public class Turn {
    
    /* Control */
    public boolean playerCantPlayFlag;
    public boolean computerCantPlayFlag;
    public boolean gameEndFlag;                 // True if game has ended
    public boolean invalidMovementFlag;         // Piece cannot be placed there!
    public boolean missMatchFlag;               // MissMatch between game info and play recived.
    
    /* Comp variables */
    public DominoPiece serverPiece;             // The play from the server
    public int serverPiecesAmmount;             // Number of pieces in comp hand
    public Pieces.Side side;                    // The side of the play of the server

    /* Game Variables */
    public int restoAmmount;                    // Number of pieces left in the resto
    
    /* Player Variables */
    public DominoPiece pieceStealed;            // The piece stealed.
    public ArrayList<DominoPiece> playerHand;   // Your hand, if is the first turn
    
    /* End game variables */
    public int scorePlayer;                     // When game's ended
    public int scoreComputer;                     // When game's ended             

    
    public Turn(){
        this.playerCantPlayFlag = false; 
        this.computerCantPlayFlag = false;
        this.gameEndFlag = false;
        this.missMatchFlag = false;
        
        this.playerHand = null;
    }
}
