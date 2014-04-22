/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view.complements;

import java.util.ArrayList;

/**
 * This class is used when the CreateGroupDialog returns a new group.
 * 
 * @author Xavi Moreno
 */
public class CreateGroupObject {
    public String name;
    public ArrayList<String> users;

    public CreateGroupObject(String name, ArrayList<String> users) {
        this.name = name;
        this.users = users;
    }
}
