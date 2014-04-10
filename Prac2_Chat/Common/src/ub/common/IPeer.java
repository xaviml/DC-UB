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

    
    // Simple chat functions
    public void userIsTyping(String username) throws RemoteException;
    public void writeMessage(Message message) throws RemoteException;

    // Group functions
    public void addGroup(String gref, String groupName, ArrayList<String> peers) throws RemoteException;
    public void userLeftGroup(String gref, String username) throws RemoteException;
    public void userJoinedGroup(String gref, String username) throws RemoteException;
    public void writeMessage(String ref, Message message) throws RemoteException;
    public void leaveGroup(String gref, String username) throws RemoteException;
    
    // State functions
    public void userConnect(String username, IPeer peer) throws RemoteException;
    public void userDisconnect(String username) throws RemoteException;
    public void notifyServerIsDown() throws RemoteException;
    public void ping() throws RemoteException;


}
