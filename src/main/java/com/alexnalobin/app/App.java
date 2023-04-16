package com.alexnalobin.app;
import com.alexnalobin.app.commandLine.Commander;
import com.alexnalobin.app.lazySender.AnswerSender;
import com.alexnalobin.app.commandLine.Conveyor;

import jakarta.websocket.Session;

public class App extends Thread{
    //863205
    Session session;
    Conveyor conv;
    Object condition;
    public App(String[] args, Session ses) {
        this.session = ses;
        this.conv = new Conveyor();
        this.condition = new Object();
    }
    @Override
    public void run() {
        Object answer_condition = new Object();
        AnswerSender send = new AnswerSender("Main Sender thread of " + session.getId(),
         Thread.currentThread(), answer_condition, conv, session);
        Commander com = new Commander("Main Commander-Core thread of " + session.getId(),
         Thread.currentThread(), condition, conv, session, answer_condition);
        com.start();
        send.start();

        try {
            com.join();
            send.join();
        } catch (InterruptedException e) {
            System.err.println("Cannot join thread. Exiting...");
        }
    }
    public void core_addCommand(String promt) {
        conv.comm.add(promt);
        synchronized (condition){
            condition.notifyAll();
        }      
    }
}