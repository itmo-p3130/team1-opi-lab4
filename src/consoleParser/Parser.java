package consoleParser;

import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        System.out.println("\u001B[32mStarted at "+ (new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()))+ " @ Made by Alex P3130 2022/2023\u001B[0m");
        while(processing_semaphore.isAlive()){
            System.out.print(">>>");
            String cdata= cin.nextLine();
            System.out.println("echo: "+cdata+" "+processing_semaphore);
        }
    }
}
