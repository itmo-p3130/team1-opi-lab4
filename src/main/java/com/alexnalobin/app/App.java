package com.alexnalobin.app;
import com.alexnalobin.app.commandLine.Commander;
import com.alexnalobin.app.consoleParser.Parser;

public class App {
    //863205
    public static void main(String[] args) {

        Object condition = new Object();
        Parser prs = new Parser("Main Parser thread",Thread.currentThread(), condition);
        Commander com = new Commander("Main Commander thread",Thread.currentThread(),condition);
        com.start();
        prs.start();

        try {
            prs.join();
            com.join();
        } catch (InterruptedException e) {
            System.err.println("Cannot join thread. Exiting...");
        }
    }

}