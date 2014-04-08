/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author zenbook
 */
public interface IPeer extends Remote{
    public void ping() throws RemoteException;
    public String getUsername() throws RemoteException;
    public void writeMessage(Message message) throws RemoteException;
    public void writeMessage(GroupReference ref, Message message) throws RemoteException;
    public void userConnect(String username, IPeer peer) throws RemoteException;
    public void userDisconnect(String username) throws RemoteException;
    public void addGroup(GroupReference gref, String groupName, ArrayList<String> peers) throws RemoteException;
    public void userIsTyping(String username) throws RemoteException;
}
