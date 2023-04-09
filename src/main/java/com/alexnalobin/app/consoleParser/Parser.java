// package com.alexnalobin.app.consoleParser;
// import java.util.ArrayList;
// import java.util.Iterator;
// import java.util.Scanner;
// import java.text.SimpleDateFormat;
// import java.util.Date;

// import com.alexnalobin.app.commandLine.Conveyor;
// import com.alexnalobin.app.dataStruct.Answer;
// import com.alexnalobin.app.dataStruct.command_condition;



// public class Parser extends Thread {
//     private final String name;
//     private Object conditor;
//     public Thread processing_semaphore;
//     public Parser(String name, Thread semaphore, Object cond){
//     /*Start logging -> Main parser thread activation*/
//         this.name = name;
//         this.processing_semaphore = semaphore;
//         this.conditor = cond;
//         this.setName(this.name);
//     }

//     @Override
//     public void run(){
//         System.out.println("\u001B[32mStarted at "+ (new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(new Date()))+ " @ Made by Alex P3130 2022/2023\u001B[0m");
//         Scanner scan = new Scanner(System.in);
//         while(processing_semaphore.isAlive()){
//             if(Conveyor.answer.size()>0) {
//                 Answer answ = Conveyor.answer.get(0);
//                 switch (answ.condition) {
//                     case finished -> {
//                         System.out.print(answ.answer + "\n");
//                         Conveyor.answer.remove(0);
//                     }
//                     case waiting_for_input -> {
//                         System.out.print(answ.answer);
//                         String cdata = scan.nextLine();
//                         sendToCommanderFirst(cdata);
//                         synchronized (conditor) {
//                             conditor.notifyAll();
//                         }
//                         Conveyor.answer.remove(0);
//                     }
//                     case started_new_command -> {
//                         Conveyor.answer.remove(0);
//                     }
//                     case critical_error -> {
//                         System.out.print("\u001B[31m" + answ.answer + "\u001B[0m");
//                         Conveyor.answer.remove(0);
//                     }
//                     case non_critical_error -> {
//                         Conveyor.answer.remove(0);
//                     }
//                     case working -> {
//                         //i++;
//                     }
//                     case ended -> {
//                         //i--;
//                     }
//                 }//Нужно чтобы двигалась очередь, а i оставался и удалял каждый элемент

//             } else {
//                 System.out.print("\u001b[34m>>>\u001b[0m");
//                 String cdata = scan.nextLine();
//                 sendToCommander(cdata);
//                 synchronized (conditor){
//                     conditor.notifyAll();
//                 }//System.out.println("Waiting for answer: "+Conveyor.answer.size()+" "+Conveyor.comm.size()+" "+Conveyor.cmdready.size());
// //                synchronized (conditor){
// //                    try{
// //                        conditor.wait();
// //                    }catch (InterruptedException e){
// //                        e.printStackTrace();
// //                    }
// //                }
//                 waitForAnswer();
//                 System.out.println("Ready with answer: " + Conveyor.answer.size());
//             }
//         }
//     }
//     private void sendToCommander(String rawCommand){
//         Conveyor.comm.add(rawCommand);
//     }
//     private void sendToCommanderFirst(String rawCommand){
//         Conveyor.comm.add(0,rawCommand);
//     }
//     private void waitForAnswer(){
//         while(Conveyor.answer.size()==0);
//     }
// }
