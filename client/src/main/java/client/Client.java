package client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.*;

import client.ipWindow.IpWindow;
import client.cardWindow.CardWindow;

public class Client {
    // FOR MACOS NEED -XstartOnFirstThread AT JVM
    static public void main(String[] args) {
        // IpWindow ip = new IpWindow();
        CardWindow win = new CardWindow();

        // int port;
        // Scanner in = new Scanner(System.in);
        // port = in.nextInt();
        // try {
        // Socket socket = new Socket("127.0.0.1",port);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }
}
