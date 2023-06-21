package client.cardsWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColoredSquare {
    public static final int NUM_SQUARES = 10;

    public ColoredSquare() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Overlapping Squares");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        SquaresPanel squaresPanel = new SquaresPanel();
        frame.add(squaresPanel);

        frame.setVisible(true);
    }
}

class SquaresPanel extends JPanel implements MouseMotionListener {
    private Square[] squares;

    public SquaresPanel() {
        setLayout(null);
        squares = new Square[ColoredSquare.NUM_SQUARES];

        for (int i = 0; i < ColoredSquare.NUM_SQUARES; i++) {
            Color color = getRandomColor();
            int size = 80;
            int x = 50+i * 40;
            int y = 400;
            squares[i] = new Square(color, size, x, y);
            add(squares[i]);
        }

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                Component component = getComponentAt(e.getPoint());
                if (component instanceof Square) {
                    Square square = (Square) component;
                    square.moveToForeground();
                }
            }

            public void mouseExited(MouseEvent e) {
                for (Square square : squares) {
                    square.moveToBackground();
                }
            }
        });
    }

    private Color getRandomColor() {
        int red = (int) (Math.random() * 256);
        int green = (int) (Math.random() * 256);
        int blue = (int) (Math.random() * 256);
        return new Color(red, green, blue);
    }

    public void mouseMoved(MouseEvent e){
        
    }

    public void mouseDragged(MouseEvent e){
        
    }
}

class Square extends JPanel {
    private Color color;
    private int size;

    public Square(Color color, int size, int x, int y) {
        this.color = color;
        this.size = size;
        setBounds(x, y, size, size);
        setOpaque(true);
        setBackground(color);
    }

    public void moveToForeground() {
        getParent().setComponentZOrder(this, 0);
        getParent().repaint();
    }

    public void moveToBackground() {
        getParent().setComponentZOrder(this, getParent().getComponentCount() - 1);
        getParent().repaint();
    }
}
