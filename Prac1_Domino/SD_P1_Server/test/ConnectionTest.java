
import java.io.IOException;
import java.net.Socket;
import model.Constants;

/*
 * Server Side
 * This project is being developed by Pablo Martinez and Xavi Moreno
 */

/**
 *
 * @author Pablo
 */
public class ConnectionTest {
    public static void main(String [] args){
        try {
            Socket s = new Socket("127.0.0.1", 8080);
            System.out.println("Success");
            while(true){s.getInputStream().read();}
        } catch (IOException ex) {
            System.err.println("CANNOT CONNECT");
        }
    }
}
