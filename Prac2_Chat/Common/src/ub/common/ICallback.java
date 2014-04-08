/*
 * Common Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.common;

import java.rmi.Remote;

/**
 *
 * @author kirtash
 */
public interface ICallback extends Remote{
    public void memberDisconnected(String username);
    public void serverDown();
}
