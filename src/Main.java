import consoleParser.Parser;
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        Parser prs = new Parser("Main thread",Thread.currentThread());
        prs.start();
        try {
            Thread.sleep(1000 * 3);
        }catch (InterruptedException e){e.printStackTrace();}

    }
}