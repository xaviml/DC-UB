/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */


package view.framework;

import view.framework.ViewController.IView;

/**
 * All View must inherit this class.
 * This class implements IView interface.
 * 
 * @author zenbook
 */
public abstract class View implements IView{
    protected ViewController parent;
    public View(ViewController parent) {
        this.parent = parent;
    }
}
