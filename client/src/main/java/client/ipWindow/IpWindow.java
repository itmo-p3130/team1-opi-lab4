package client.ipWindow;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.tools.Tool;

public class IpWindow {
    public IpWindow(){
        JFrame window = new JFrame("IP Grabber");
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenXSize = screen.width;
        int screenYSize = screen.height;
        double scale = 0.45;
        double YbyX = 0.45;
        window.setSize((int)(screenXSize*scale),(int)(screenXSize * scale * YbyX));
        int xsize = window.getWidth();
        int ysize = window.getHeight();
        JButton buttonConnect = new JButton("Connect");
        buttonConnect.setSize((int) (xsize * 0.45), (int) (ysize * 0.33));
        buttonConnect.setLocation((int) (xsize * 0.025), (int) (ysize * 0.622));
        
        JButton buttonExit = new JButton("Exit");
        buttonExit.setSize((int) (xsize * 0.45), (int) (ysize * 0.33));
        buttonExit.setLocation((int) (xsize * 0.50625), (int) (ysize * 0.622));
        
        JTextField textFieldIp = new JTextField("127.0.0.1:00001", 15);
        textFieldIp.setSize((int) (xsize *0.25* 0.93125), (int) (ysize *0.25* 0.2277));
        textFieldIp.setLocation(0,0);
        textFieldIp.setToolTipText("Write an IP of the server you want to connect using form: 0.0.0.0:00001");
        textFieldIp.setFont(new Font("Monospaced",Font.LAYOUT_LEFT_TO_RIGHT,15 ));

        window.add(buttonConnect);
        window.add(buttonExit);
        window.add(textFieldIp);
        window.setResizable(false);
        window.setVisible(true);
    }
}
