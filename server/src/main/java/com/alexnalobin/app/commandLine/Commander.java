package com.alexnalobin.app.commandLine;
import com.alexnalobin.app.commandLine.Commands.*;
import com.alexnalobin.app.dataStruct.Answer;
import com.alexnalobin.app.dataStruct.command_condition;

import jakarta.websocket.Session;

import java.util.*;

public class Commander extends Thread {
    private final String name;
    private Object conditor;
    public Thread processing_semaphore;
    public Conveyor conveyor;
    public Session session;
    public Object answer_conditor;
    public Commander(String name, Thread semaphore, Object cond, Conveyor conv, Session session, Object answcond){
        this.name = name;
        this.processing_semaphore=semaphore;
        this.conditor = cond;
        this.setName(this.name);
        this.conveyor = conv;
        this.session = session;
        this.answer_conditor = answcond;

    }
    private void nextCommand(){
        
                return;
    }
    @Override
    public void run(){
        while (processing_semaphore.isAlive()){
            if(conveyor.comm.size() == 0 & conveyor.cmdready.size() == 0){
                synchronized (conditor){
                    try{
                        conditor.wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            if(!(conveyor.comm.size() == 0)){
                nextCommand();
            }

            if(!(conveyor.cmdready.size() == 0)){
                command current_command = conveyor.cmdready.get(0);
                try {
                    current_command.execute();
                }catch (InterruptedException e){
                    System.err.println(e);
                }
                synchronized (answer_conditor) {
                    answer_conditor.notifyAll();
                }
                conveyor.cmdready.remove(0);
                conveyor.comm_buff.remove(0);
            }
            
        }
    }
    private void addCommandToQueue(command com){
        conveyor.cmdready.add(com);
        conveyor.history.add(com);
    }
    
    public void addAnswer(Answer answer){
        conveyor.answer.add(answer);
        synchronized (answer_conditor) {
            answer_conditor.notifyAll();
        }
    }
}
