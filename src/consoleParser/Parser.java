package consoleParser;

import java.util.Scanner;

public class Parser extends Thread{
    private final String name;
    public Thread processing_semaphore;
    public Parser(String name, Thread semaphore){
    /*Start logging -> Main parser thread activation*/
        this.name=name;
        this.processing_semaphore=semaphore;
    }
    @Override
    public void run(){
        Scanner cin = new Scanner(System.in);
        while(processing_semaphore.isAlive()){
            System.out.print(">>>");
            String cdata= cin.nextLine();
            System.out.println("echo: "+cdata+" "+processing_semaphore);
        }
    }
}
