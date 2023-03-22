import commandLine.Commander;
import consoleParser.Parser;

public class Main {
    //863205
    public static void main(String[] args) {


        Parser prs = new Parser("Main parser thread",Thread.currentThread());
        Commander com = new Commander("Main commander thread",Thread.currentThread());
        com.start();
        prs.start();

        while(prs.isAlive() && com.isAlive()) { }

        try {
            prs.join();
            com.join();
        } catch (InterruptedException e) {
            System.err.println("Cannot join thread. Exiting...");
        }
    }

}