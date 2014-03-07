/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.swd.model.connection;

/**
 *
 * @author Xavi Moreno
 */
public class ProtocolError {
    
    public enum ErrorType{SYNTAX_ERR,
                            ILLEGAL_ACTION_ERR,
                            NOT_ENOUGH_RESOURCES_ERR,
                            INTERNAL_SERVER_ERR,
                            UNDEFINED_ERR,
                            }
    
    public ErrorType type;
    public String msg;
    
    public ProtocolError(ErrorType type, String msg) {
        this.type = type;
        this.msg = msg;
    }

}
