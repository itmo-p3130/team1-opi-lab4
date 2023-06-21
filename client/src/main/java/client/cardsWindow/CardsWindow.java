package client.cardsWindow;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CardsWindow {
        JFrame  window;
        public CardsWindow(){
            this.window = new JFrame("Card game", null);
            JPanel base = new JPanel(new FlowLayout(0), false);
            base.add(loadImg("G:/Мой диск/Lab5_CommandClient/PokerTable.png"));
            window.add(base);
            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            
        }
        public JPanel loadImg(String path){
                try {
                final BufferedImage image = ImageIO.read(new File(path));
                JPanel panel = new JPanel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(image, 0, 0, null);
                    }
                };
                panel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                panel.setOpaque(false);
                return panel;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
}
