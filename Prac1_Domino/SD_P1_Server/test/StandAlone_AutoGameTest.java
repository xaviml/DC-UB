/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.ArrayList;
import model.game.DominoPiece;
import model.game.GameController;
import model.game.Pieces;
import model.game.Turn;

/**
 *
 * @author Pablo
 */
public class StandAlone_AutoGameTest {
    public static void main(String [] args){
        ArrayList<DominoPiece> hand;
        ArrayList<DominoPiece> table;
        table = new ArrayList<>();
        
        GameController g = new GameController(null);
        Turn t = g.initGame();
        hand = t.playerHand;
        
        printHand(hand);
        
        if (t.serverPiece != null){
            table.add(t.serverPiece);
        }
        boolean th,side;
        while (!t.gameEndFlag){
            th = false;
            side = false;
            for(DominoPiece dp: hand){
                t = g.throwing(dp, Pieces.Side.LEFT);
                if (t.missMatchFlag){
                    t = g.throwing(dp, Pieces.Side.RIGHT);
                    side = true;
                }
                if (!t.missMatchFlag) {
                    th = true;
                    if (!side){
                        table.add(0, dp);
                    }
                    else{
                        table.add(dp);
                    }
                    
                    break;
                }
            }
            if (!th){
                t = g.steal();
                if (t.pieceStealed != null){
                    hand.add(t.pieceStealed);
                }
            }
            if (t.serverPiece != null){
                if (t.side == Pieces.Side.LEFT){
                    table.add(0, t.serverPiece);
                }
                else{
                    table.add(t.serverPiece);
                }
            }
            
            // End of the turn
            //System.out.println("PLAYER HAND");
            //printHand(hand);
            
        }
        System.out.println(" SCORE COMPUTER -> "+t.scoreComputer);
        System.out.println(" SCORE PLAYER -> "+t.scorePlayer);
 
    }
    
    public static void printHand(ArrayList<DominoPiece> l){
        System.out.println("---------------");
        for (DominoPiece p: l){
            System.out.println(p);
        }
        System.out.println("---------------");
        
    }
    
}
