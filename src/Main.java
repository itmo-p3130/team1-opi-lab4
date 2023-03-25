import commandLine.Commander;
import consoleParser.Parser;

public class Main {
    //863205
    public static void main(String[] args) {

        Object condition = new Object();
        Parser prs = new Parser("Main parser thread",Thread.currentThread(), condition);
        Commander com = new Commander("Main commander thread",Thread.currentThread(),condition);
        com.start();
        prs.start();

        try {
            prs.join();
            com.join();
        } catch (InterruptedException e) {
            System.err.println("Cannot join thread. Exiting...");
        }
    }

}