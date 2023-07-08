package client.cardWindow;


import org.jsfml.graphics.*;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.system.Vector3f;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;

public class CardWindow{
    public RenderWindow window;
    private View view;

    private Vector2i windowPos;
    private Vector2f worldPos;
    private Vector2f screenSize;
    private Sprite  pokerTable;

    public CardWindow(){
        screenSize = this.getScreenSize();
        int size = (int) screenSize.x / 2;
        window = new RenderWindow(new VideoMode(size,size/4*3), "Cards Online");
        view = (View)  window.getDefaultView();
        view.setSize(640,480);
        view.setCenter(320,240);
        window.setView(view);
        System.out.println(view.getCenter()); //500-500
        System.out.println(view.getViewport());//0 0 1 1
        System.out.println(view.getSize());// 1000 1000
        init();
        while(window.isOpen()){
            window.setView(view);
            window.clear(new Color(20, 120, 32, 255));
            window.draw(pokerTable);
            window.display();
            for(org.jsfml.window.event.Event event: window.pollEvents()){
                if(event.type == org.jsfml.window.event.Event.Type.CLOSED){
                    window.close();
                }
                if(event.type == org.jsfml.window.event.MouseEvent.Type.MOUSE_BUTTON_PRESSED){
                    windowPos = Mouse.getPosition(window);
                    worldPos = window.mapPixelToCoords(windowPos);
                    System.out.println(worldPos);
                }
            }
        }
    }
    private void init(){
        try{
            Image imgloader;
            Texture greenBackground;
            imgloader = new Image();
            greenBackground = new Texture();
            imgloader.loadFromFile(Paths.get("Resources/PokerTable.png"));
            greenBackground.loadFromImage(imgloader);
            greenBackground.setRepeated(true);
            greenBackground.setSmooth(true);
            pokerTable = new Sprite(greenBackground);
            pokerTable.setTextureRect(new IntRect(0,0,1280,960));
            pokerTable.setScale(0.5f,0.5f);
        } catch (IOException | TextureCreationException e) {
            throw new RuntimeException(e);
        }
    }
    private void initCards(){
//        try {
//
//        }
    }
    private Vector2f getScreenSize(){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Vector2f screenSize = new Vector2f(width, height);
        return screenSize;
    }
}