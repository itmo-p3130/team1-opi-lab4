package client.ipWindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.tools.Tool;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import client.cardWindow.Cards.Card;
import client.cardWindow.Cards.CardNum;
import client.cardWindow.Cards.CardSuit;
import client.conveyor.Conveyor;
import client.conveyor.Request;
import client.conveyor.RequestConstants;
import client.conveyor.User;
import client.conveyor.ConveyorListener;

public class IpWindow {
    private Conveyor conveyor;

    public IpWindow(Conveyor con) {
        this.conveyor = con;
        conveyor.client.getKryo().register(Request.class);
        conveyor.client.getKryo().register(Object.class);
        conveyor.client.getKryo().register(String.class);
        conveyor.client.getKryo().register(Card.class);
        conveyor.client.getKryo().register(HashMap.class);
        conveyor.client.getKryo().register(RequestConstants.class);
        conveyor.client.getKryo().register(Card.class);
        conveyor.client.getKryo().register(CardNum.class);
        conveyor.client.getKryo().register(CardSuit.class);
        conveyor.client.getKryo().register(ArrayList.class);
        conveyor.client.getKryo().register(Vector.class);
        conveyor.client.getKryo().register(User.class);
        conveyor.client.addListener(new ConveyorListener(this.conveyor) {
            public void received(Connection connection, Object obj) {
                if (obj instanceof Request) {
                    Request req = (Request) obj;
                    req.setConnection(connection);
                    conveyor.requests.add(req);
                    System.err.println("Ner req: " + req.getType());
                    synchronized (conveyor.requests) {
                        conveyor.requests.notifyAll();
                    }
                }
            }

            @Override
            public void disconnected(com.esotericsoftware.kryonet.Connection connection) {
                System.err.println("Disconnected from server");
            }

            @Override
            public void connected(com.esotericsoftware.kryonet.Connection connection) {
                conveyor.userID = connection.getID();
                Request req = new Request(conveyor.userID);
                req.setType(RequestConstants.CONNECT_TO_GAME_SESSION);
                req.addData(RequestConstants.CONNECT_TO_GAME_SESSION, conveyor.userID);
                connection.sendTCP(req);
                System.err.println("Connected to server");
            }
        });
        conveyor.client.start();
        JFrame frame = new JFrame("Ip Grabber");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        FlowLayout flw = new FlowLayout();
        JPanel panel = new JPanel(flw);
        JPanel panel2 = new JPanel(flw);
        JLabel label = new JLabel("Enter server's IP:");
        JLabel label2 = new JLabel("Enter games's name:");
        JTextField textField = new JTextField(17);

        textField.setText("127.0.0.1:15000");

        JTextField nameField = new JTextField(17);
        JButton exitButton = new JButton("Exit");
        JButton connectButton = new JButton("Connect");

        panel.add(label);
        panel.add(textField);
        panel2.add(label2);
        panel2.add(nameField);
        // panel.add(panel2);
        panel.add(exitButton);
        panel.add(connectButton);

        frame.add(panel);
        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Connecting to " + textField.getText());
                String ipAddressRegex = "^(.*?):(\\d+)$";
                Pattern pattern = Pattern.compile(ipAddressRegex);
                Matcher matcher = pattern.matcher(textField.getText());

                if (matcher.matches()) {
                    String ipAddress = matcher.group(1);
                    String port = matcher.group(2);

                    System.out.println("IP Address: " + ipAddress);
                    System.out.println("Port: " + port);
                    try {
                        int intValue = Integer.parseInt(port);
                        conveyor.client.connect(5000, ipAddress, intValue);
                        if (conveyor.client.isConnected()) {
                            synchronized (conveyor.client) {
                                conveyor.client.notifyAll();
                            }
                            // CLOSE HERE
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(connectButton);
                                    frame.dispose();
                                }
                            });
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid integer format in port");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    System.out.println("Invalid input format");
                }

            }
        });
    }
}
