/*
 * Peer Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package ub.view;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Xavi Moreno
 */
public class GroupListModel extends AbstractListModel<String>{
    
    private ArrayList<MessageBox> mArray;

    public GroupListModel(ArrayList<MessageBox> mArray) {
        this.mArray = mArray;
    }
    
    @Override
    public int getSize() {
        return mArray.size();
    }

    @Override
    public String getElementAt(int i) {
        return mArray.get(i).getNameChat();
    }
}
