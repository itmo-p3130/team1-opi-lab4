import consoleParser.Parser;
public class Main {
    public static void main(String[] args) {
        Parser prs = new Parser("Main parser thread",Thread.currentThread());
        prs.start();
        while(prs.isAlive()){}//I'll change it to prs.join(), but later
    }
}