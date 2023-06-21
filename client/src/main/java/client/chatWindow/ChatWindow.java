package client.chatWindow;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatWindow {
    private JPanel chatPanel;
    private JTextField inputField;
    private JFrame frame;
    private int width;
    private int height;
    public ChatWindow() {
        frame = new JFrame("Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        width = screenWidth / 2;
        height = screenHeight / 2;
        frame.setPreferredSize(new Dimension(width, height));

        inputField = new JTextField();

        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        JScrollPane chatScroll = new JScrollPane(chatPanel);
        chatScroll.getVerticalScrollBar().setUnitIncrement(20);

        frame.getContentPane().add(chatScroll, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void addMessage(String message, String avatarPath) {
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        ImageIcon img = new ImageIcon("G:/Мой диск/Ivatolm.jpg");
        int newWidth = width / 6;
        int newHeight = height / 6;
        int heightBorder = height/8;
        ImageIcon var = new ImageIcon(img.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
        JLabel avatarLabel = new JLabel(var);
        avatarLabel.setBorder(new EmptyBorder(heightBorder, heightBorder, heightBorder, heightBorder));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());

        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        // messageArea.setForeground(new Color(255, 255, 255));
        // messageArea.setBackground(new Color(25, 44, 69));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        JLabel timeLabel = new JLabel(dtf.format(now));
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        textPanel.add(timeLabel, BorderLayout.NORTH);
        textPanel.add(avatarLabel, BorderLayout.WEST);

        int maxWidth = frame.getContentPane().getWidth() -
                avatarLabel.getPreferredSize().width;
        messageArea.setSize(new Dimension(maxWidth, Short.MAX_VALUE));
        messageArea.setPreferredSize(new Dimension(maxWidth,
                messageArea.getPreferredSize().height));

        textPanel.add(messageArea, BorderLayout.CENTER);
        messagePanel.add(textPanel, BorderLayout.CENTER);
        chatPanel.add(messagePanel);
        frame.revalidate();
        frame.repaint();

        // JPanel messagePanel = new JPanel();
        // messagePanel.setLayout(new BorderLayout());

        // JLabel avatarLabel = new JLabel(new ImageIcon(avatarPath));
        // messagePanel.add(avatarLabel, BorderLayout.WEST);

        // JTextArea messageArea = new JTextArea(message);
        // messageArea.setEditable(false);
        // messageArea.setLineWrap(true);
        // messageArea.setWrapStyleWord(true);

        // int maxWidth = frame.getContentPane().getWidth() -
        // avatarLabel.getPreferredSize().width;
        // messageArea.setSize(new Dimension(maxWidth, Short.MAX_VALUE));
        // messageArea.setPreferredSize(new Dimension(maxWidth,
        // messageArea.getPreferredSize().height));

        // messagePanel.add(messageArea, BorderLayout.CENTER);

        // chatPanel.add(messagePanel);

        // frame.revalidate();
        // frame.repaint();
    }

    public void closeWindow() {
        frame.dispose();
    }

    public boolean isVisible() {
        return frame.isVisible();
    }
}
