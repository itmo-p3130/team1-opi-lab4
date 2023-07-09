package client.cardWindow;


import org.jsfml.graphics.*;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.system.Vector3f;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Vector;
import java.util.function.BiConsumer;

public class CardWindow{
    public RenderWindow window;
    private View view;
    private Random rnd;
    private Vector2i windowPos;
    private Vector2f worldPos;
    private Vector2f screenSize;
    private Sprite  pokerTable;
    private Clock frClock;
    private Vector<Card> cards;

    public CardWindow(){
        init();
        System.out.println(view.getCenter()); //500-500
        System.out.println(view.getViewport());//0 0 1 1
        System.out.println(view.getSize());// 1000 1000

        while(window.isOpen()){
            window.clear(new Color(20, 120, 32, 255));
            window.draw(pokerTable);
            for (int i =0;i!=13*4;i++){
                cards.elementAt(i).draw(window);
            }
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
        initPokerTable();
        initCards();
        initWindowAndView();
    }
    private void initPokerTable(){
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
            pokerTable.setScale(1.0f,1.0f);
        } catch (IOException | TextureCreationException e) {
            throw new RuntimeException(e);
        }
    }
    private void initCards(){
        try {
            Image imageCards;
            imageCards = new Image();
            imageCards.loadFromFile(Paths.get("Resources/Cards.png"));
            imageCards.createMaskFromColor(new Color(85, 170, 85,255));
            Texture textureCards = new Texture();
            textureCards.loadFromImage(imageCards);
            Sprite sprite = new Sprite(textureCards);
            cards = new Vector<>();
            for (int i = 0; i != 13; i++) {
                for (int j = 0; j!=4; j++){
                    sprite.setTextureRect(new IntRect(30+390*i, 30+568*j, 360, 538));
                    sprite.setPosition(15*i*2+120*j*2,i*57);
                    sprite.setScale(0.5f,0.5f);
                    cards.add(new Card(sprite, CardNum.Ace,CardSuit.Tiles));
                    sprite = new Sprite(textureCards);
                }
            }

        } catch (IOException | TextureCreationException e) {
            throw new RuntimeException(e);
        }
    }
    private void initWindowAndView(){
        screenSize = this.getScreenSize();
        int size = (int) screenSize.x / 2;
        window = new RenderWindow(new VideoMode(size,size/4*3), "Cards Online");
        view = (View)  window.getDefaultView();
        view.setSize(640*2,480*2);
        view.setCenter(320*2,240*2);
        window.setView(view);
    }
    private Vector2f getScreenSize(){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Vector2f screenSize = new Vector2f(width, height);
        return screenSize;
    }
}