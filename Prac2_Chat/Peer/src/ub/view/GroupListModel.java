/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view;

import java.util.ArrayList;
import javax.swing.AbstractListModel;
import ub.common.GroupReference;

/**
 *
 * @author Xavi Moreno
 */
public class GroupListModel extends AbstractListModel<GroupObject>{
    
    private ArrayList<GroupObject> mArray;

    public GroupListModel(ArrayList<GroupObject> mArray) {
        this.mArray = mArray;
    }
    
    @Override
    public int getSize() {
        return mArray.size();
    }

    @Override
    public GroupObject getElementAt(int i) {
        return mArray.get(i);
    }
}

class GroupObject {
    private GroupReference id;
    private String name;

    public GroupObject(GroupReference id, String name) {
        this.id = id;
        this.name = name;
    }

    public GroupReference getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
