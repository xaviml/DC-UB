/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.common;

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
    public boolean writeMessage(Message message) throws RemoteException;
    public boolean writeMessageGroup(GroupReference ref, Message message) throws RemoteException;
    public void userConnect(String username, IPeer peer) throws RemoteException;
    public void userDisconnect(IPeer peer) throws RemoteException;
    public void addGroup(float idChat, String group, IPeer[] peers) throws RemoteException;
    public void userIsTyping() throws RemoteException;
}
