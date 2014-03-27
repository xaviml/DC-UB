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
public interface IServer extends Remote {
    public void registryUser(IPeer peer) throws RemoteException;
    public void unregistryUser(IPeer peer) throws RemoteException;
    //public IPeer[] getUsers();
    public IPeer getUser(String user) throws RemoteException;
}
