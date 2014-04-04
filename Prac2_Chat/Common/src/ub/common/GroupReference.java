/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.common;

import java.io.Serializable;

/**
 *
 * @author Pablo
 */
public class GroupReference implements Serializable {
    private long unique;
    
    public GroupReference(){
        this.unique = (System.currentTimeMillis()*1000)+(System.nanoTime()%1000);
    }
}
