package client.ipWindow;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.tools.Tool;

public class IpWindow {
    public IpWindow() {
        JFrame frame = new JFrame("Ip Grabber");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new FlowLayout());
        JLabel label = new JLabel("Enter server's IP:");
        JTextField textField = new JTextField(17);
        JButton exitButton = new JButton("Exit");
        JButton connectButton = new JButton("Connect");

        panel.add(label);
        panel.add(textField);
        panel.add(exitButton);
        panel.add(connectButton);

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // exitButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // System.exit(0);
        // }
        // });

        // connectButton.addActionListener(new ActionListener() {
        // @Override
        // public void actionPerformed(ActionEvent e) {
        // System.out.println("Connecting to " + textField.getText());
        // }
        // });
    }
}
