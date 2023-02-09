import consoleParser.Parser;
public class Main {
    static boolean processing_semaphore = true;
    public static void main(String[] args) {

        System.out.println("Hello world!");
        Parser prs = new Parser("Main thread",processing_semaphore);
        prs.run();
        while (true){;;}
    }
}