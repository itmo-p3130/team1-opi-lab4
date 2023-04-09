package com.alexnalobin.app;
import com.alexnalobin.app.commandLine.Commander;
//import com.alexnalobin.app.consoleParser.Parser;
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
        
        // Parser prs = new Parser("Main Parser thread of " +
        // session.getId(),Thread.currentThread(), condition);
        Commander com = new Commander("Main Commander-Core thread of " + session.getId(), Thread.currentThread(), condition, conv);
        com.start();
        // prs.start();

        try {
            // prs.join();
            com.join();
        } catch (InterruptedException e) {
            System.err.println("Cannot join thread. Exiting...");
        }
    }
    public void core_addCommand(String promt) {
        conv.comm.add(promt);
        synchronized (condition){
        condition.notifyAll();
        }

        try {
            session.getBasicRemote().sendText(String.valueOf(conv.comm.size()));
        } catch (Exception e) {
            // TODO: handle exception
            System.err.print(e);
        }
        
    }

}