package client;

import client.conveyor.Commander;
import client.conveyor.Conveyor;
import client.ipWindow.IpWindow;
import client.cardWindow.CardWindow;

public class Client {
    // FOR MACOS NEED -XstartOnFirstThread AT JVM
    static public void main(String[] args) {
        Conveyor conveyor = new Conveyor();
        IpWindow ip = new IpWindow(conveyor);
        synchronized (conveyor.client) {
            try {
                conveyor.client.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Commander cmd = new Commander(conveyor);
        cmd.start();
        System.err.println("Start CardWindow");
        CardWindow win = new CardWindow(conveyor);
        conveyor.client.close();
        System.err.println("Stop Kryonet client");
        System.err.println("Stop CardWindow");
    }
}
