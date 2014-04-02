/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.common;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author zenbook
 */
public interface IPeer extends Remote {
    /**
     * Username of Peer.
     * 
     * @return String
     * @throws java.rmi.RemoteException
     */
    public String getUsername() throws RemoteException;
    public void writeMessage(float idChat, IPeer peer, String message);
    public void userConnect(String username, IPeer peer) throws RemoteException;
    public void userDisconnect(IPeer peer) throws RemoteException;
    public void addGroup(float idChat, String group, IPeer[] peers) throws RemoteException;
    public void userIsTyping();
}
