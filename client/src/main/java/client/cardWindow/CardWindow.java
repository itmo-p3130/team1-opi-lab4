package client.cardWindow;


import org.jsfml.graphics.*;
import org.jsfml.window.VideoMode;

public class CardWindow{
    public CardWindow(){
        RenderWindow window = new RenderWindow(new VideoMode(640,480), "JSFML Window");
        while(window.isOpen()){
            window.clear(Color.CYAN);
            window.display();
        }
    }
}