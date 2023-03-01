package consoleParser;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;
import commandLine.conveyor;
import dataStruct.Answer;

public class Parser extends Thread implements conveyor{
    private final String name;
    public Thread processing_semaphore;
    public Parser(String name, Thread semaphore){
    /*Start logging -> Main parser thread activation*/
        this.name=name;
        this.processing_semaphore=semaphore;
    }
    @Override
    public void run(){
        Scanner scan = new Scanner(System.in);
        System.out.println("\u001B[32mStarted at "+ (new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()))+ " @ Made by Alex P3130 2022/2023\u001B[0m");
        while(processing_semaphore.isAlive()){
            if(conveyor.answ.size()>0){
                Answer answ = conveyor.answ.get(0);
                switch (answ.condition){
                    case finished :
                        break;
                    default:
                        break;
                }
            }else {
                System.out.print(">>>");
                String cdata = scan.nextLine();
                sendToCommander(cdata);//try{Thread.sleep(100);}catch (InterruptedException ex){;};
                waitForAnswer();
                Answer answ = conveyor.answ.get(0);

                if(answ.answer.length() != 0) {
                    System.out.println(answ.answer);
                }
                conveyor.answ.remove(0);
                System.out.println("answ:" + conveyor.answ.size() + " comm:" + conveyor.comm.size() + " comready:" + conveyor.commands_ready.size());
            }
            //System.out.println("echo: "+cdata+" "+cdata.length());
            //conveyor.comm.add(cdata);
        }
    }
    private void sendToCommander(String rawCommand){
        conveyor.comm.add(rawCommand);
    }
    private void waitForAnswer(){
        while(conveyor.answ.size()==0){;}
    }

}
