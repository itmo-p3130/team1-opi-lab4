package consoleParser;

import java.util.Scanner;

public class Parser extends Thread{
    private final String name;
    private boolean processing_semaphore;
    public Parser(String name, boolean semaphore){
    /*Start logging -> Main parser thread activation*/
        this.name=name;
        this.processing_semaphore=semaphore;
    }
    public void run(){
        Scanner cin = new Scanner(System.in);
        while(processing_semaphore){
            System.out.print(">>>");
            String cdata= cin.next();
            System.out.println("echo: "+cdata);
        }
    }
}
