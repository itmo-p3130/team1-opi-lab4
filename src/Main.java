import commandLine.Commander;
import consoleParser.Parser;
public class Main {
    //863205
    public static void main(String[] args) {
        Parser prs = new Parser("Main parser thread",Thread.currentThread());
        Commander com = new Commander("Main commander thread",Thread.currentThread());
        prs.start();
        try{Thread.sleep(5000);}catch (InterruptedException ex){;}
        com.printAll();
        while(prs.isAlive()){}//I'll change it to prs.join(), but later
    }
}