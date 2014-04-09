package ub.model;

import ub.common.IPeer;

public interface ChatModelServices{
    public String getMyUserName();
    public IPeer getIPeerByName(String username);
    public void notifyDisconnectedClient(String username);
    public void notifyServerDown();
}