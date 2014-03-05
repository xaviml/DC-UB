/*
 * Client Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

package view;

import controller.Controller;
import controller.GameController;
import view.views.MainView;
import view.views.View;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.views.Bundle;

/**
 *
 * @author Xavi Moreno
 */
public class ViewController {
    private Scanner sc;
    private View mCurrentView;
    
    /**
     * This is the stack structure where the views are stacked.
     * 
     */
    
    private Stack<View> stackViews;
    
    /**
     * This HashMap connect a view with another.
     * 
     */
    private Bundle mBundle;
    
    /**
     * This is the game controller. This controller controls the socket connections
     * 
     */
    private Controller mController;
    
    public ViewController(String[] args) {
        this.sc = new Scanner(System.in);
        /*View principal: MainView*/
        mCurrentView = getView(MainView.class);
        stackViews = new Stack<>();
        mBundle = new Bundle();
        String[] ipport = args[1].split(":");
        mController = new Controller(ipport[0], Integer.parseInt(ipport[1]));
    }
    
    /**
     * Main loop of this framework.
     * 
     */
    
    public void exec() {
        Class nextView;
        /*Call the current view until the current view is null*/
        while(mCurrentView != null) {
            try {
                /*Clear screen if SO allows it*/
                clearConsole();
                
                /*Show the title*/
                System.out.println("\n\n\n***************************************");
                System.out.println("\t"+mCurrentView.getTitle());
                System.out.println("***************************************\n");
                
                /*Show in screen the current view*/
                nextView = mCurrentView.run(sc);
                
                /*If the current view doesn't want navigate to other view*/
                if(nextView == null) {
                        mCurrentView = stackViews.isEmpty() ? null : stackViews.pop();
                }else{
                    /*Check if 'nextView' is a View*/
                    if(!View.class.isAssignableFrom(nextView)) {
                        throw new Exception(nextView.getName() + " doesn't implement IView interface");
                    }
                    mCurrentView = getView(nextView);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private View getView(Class c) {
        try {
            Constructor cons = c.getConstructor(ViewController.class);
            View v = (View) cons.newInstance(new Object[] {this});
            return v;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * If the view want save in the stack memory, must call this method.
     * 
     */
    
    public void saveView() {
        stackViews.push(this.mCurrentView);
    }
    
    public Bundle getBundle() { return mBundle; }
    public Controller getController() { return mController; }

    public void clearConsole() throws IOException {
        System.out.print("\u001b[2J");
        System.out.flush();
        /*
        String os = System.getProperty("os.name");

        if (os.contains("Windows")) {
            Runtime.getRuntime().exec("cls");
        }
        else {
            Runtime.getRuntime().exec("clear");
        }*/
    }
    
    public interface IView {
        
        /**
         * This is the title that will appear in console.
         * 
         * @return 
         */
        public String getTitle();
        
        /**
         * This method will be implemented by a View class. 
         * Must be return a view class, the next view that will be shown.
         * If the the return class is null, the view controller will consider
         * that this view will be closed and it will show the previuos View
         * contained in stackView.
         * 
         * The Scanner instance 
         * 
         * @param sc
         * @return 
         */
        public Class run(Scanner sc);
    }
    
}
