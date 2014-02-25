/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */


package view.views;

import view.ViewController;
import view.ViewController.IView;

/**
 *
 * @author zenbook
 */
public abstract class View implements IView{
    protected ViewController parent;
    public View(ViewController parent) {
        this.parent = parent;
    }
}
