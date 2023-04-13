package com.alexnalobin.app.lazySender;

import jakarta.websocket.Session;
import com.alexnalobin.app.commandLine.Conveyor;

public class AnswerSender extends Thread {
    private final String name;
    private Object conditor;
    public Thread processing_semaphore;
    public Conveyor conveyor;
    public Session session;
    public AnswerSender (String name, Thread semaphore, Object cond, Conveyor conv, Session ses) {
        this.name = name;
        this.processing_semaphore = semaphore;
        this.conditor = cond;
        this.setName(this.name);
        this.conveyor = conv;
        this.session = ses;
    }
    @Override
    public void run(){
        while(processing_semaphore.isAlive() || !conveyor.answer.isEmpty()){
            if(conveyor.answer.isEmpty()){
                synchronized (conditor) {
                    try {
                        conditor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                try {
                    session.getBasicRemote().sendText(conveyor.answer.get(0).answer);
                    conveyor.answer.remove(0);
                } catch (Exception e) {
                    System.err.print(e);
                }
            }
        }
    }
}
