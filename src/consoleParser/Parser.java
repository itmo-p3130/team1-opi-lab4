package consoleParser;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

import commandLine.Conveyor;
import dataStruct.Answer;
public class Parser extends Thread {
    private final String name;
    public Thread processing_semaphore;
    public Parser(String name, Thread semaphore){
    /*Start logging -> Main parser thread activation*/
        this.name = name;
        this.processing_semaphore = semaphore;
    }
    @Override
    public void run(){
        System.out.print("\u001B[32mStarted at "+ (new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()))+ " @ Made by Alex P3130 2022/2023\u001B[0m");
        Scanner scan = new Scanner(System.in);
        while(processing_semaphore.isAlive()){
            if(Conveyor.answer.size()>0) {
                Answer answ = Conveyor.answer.get(0);
                System.out.print(answ.answer);
                Conveyor.answer.remove(0);
            } else {
                System.out.print("\n>>>");
                String cdata = scan.nextLine();
                sendToCommander(cdata);
                waitForAnswer();
                System.out.println("answ:" + Conveyor.answer.size() + " comm:" + Conveyor.cmd.size() + " comready:" + Conveyor.cmdready.size());
            }
        }
    }
    private void sendToCommander(String rawCommand){
        Conveyor.cmd.add(rawCommand);
    }
    private void waitForAnswer(){
        while(Conveyor.answer.size()==0);
    }
}
