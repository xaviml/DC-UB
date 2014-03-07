/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package model;

import ub.swd.model.connection.AbstractProtocol;

/**
 *
 * @author Xavi Moreno
 */
public class StatMatch {

    public AbstractProtocol.Winner winner;
    public String ipServer;

    public StatMatch(AbstractProtocol.Winner winner, String ipServer) {
        this.winner = winner;
        this.ipServer = ipServer;
    }
}
