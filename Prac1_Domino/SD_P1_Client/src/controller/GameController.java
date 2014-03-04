/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package controller;

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
        return null;
    }
    
    public String getUserName() {
        return this.username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
}
