/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

import model.DominoGame;

/**
 *
 * @author Xavi Moreno
 */
public class GameController {
    String username;
    
    
    
    public GameController() {
        this.username = "Player";
    }
    
    
    public DominoGame createGame() {
        return new DominoGame();
    }
    
    public String getUserName() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
}
