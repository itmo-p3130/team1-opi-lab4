package com.alexnalobin.app;
import com.alexnalobin.app.commandLine.Commander;
import com.alexnalobin.app.consoleParser.Parser;

import jakarta.websocket.Session;



public class App extends Thread{
    //863205
    Session session;
    public App(String[] args, Session ses) {
        this.session = ses;
        Object condition = new Object();
        //Parser prs = new Parser("Main Parser thread of " + session.getId(),Thread.currentThread(), condition);
        Commander com = new Commander("Main Commander thread of " + session.getId(),Thread.currentThread(),condition);
        com.start();
        //prs.start();

        try {
            //prs.join();
            com.join();
        } catch (InterruptedException e) {
            System.err.println("Cannot join thread. Exiting...");
        }
    }

}