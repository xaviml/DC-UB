/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ub.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author zenbook
 */
public interface IServer extends Remote {
    public ConcurrentHashMap<String,IPeer> registryUser(String username, IPeer peer) throws RemoteException, InvalidUserNameException;
    public void unregistryUser(String username) throws RemoteException;
    public IPeer getUser(String user) throws RemoteException;
    public ArrayList<String> getUsers();
}
