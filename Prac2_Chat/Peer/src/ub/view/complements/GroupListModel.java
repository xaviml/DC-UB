/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view.complements;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 * This class is used to show the list of the groups.
 * 
 * @author Xavi Moreno
 */
public class GroupListModel extends AbstractListModel<String>{
    
    private ArrayList<MessageBox> mArray;

    public GroupListModel(ArrayList<MessageBox> mArray) {
        this.mArray = mArray;
    }
    
    /**
     * Return the size of the array.
     * 
     * @return 
     */
    
    @Override
    public int getSize() {
        return mArray.size();
    }
    
    /**
     * Return the i-th element of the list
     * 
     * @param i
     * @return 
     */

    @Override
    public String getElementAt(int i) {
        return mArray.get(i).getNameChat();
    }
}
