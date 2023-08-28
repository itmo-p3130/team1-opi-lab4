package client.cardWindow;

import client.cardWindow.Animation.Animation;
import client.cardWindow.Cards.Card;
import client.cardWindow.Cards.CardNum;
import client.cardWindow.Cards.CardSuit;
import client.cardWindow.Players.Player;
import client.conveyor.Conveyor;

import org.jsfml.audio.Sound;
import org.jsfml.graphics.*;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Image;
import org.jsfml.system.*;
import org.jsfml.window.ContextActivationException;
import org.jsfml.window.ContextSettings;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.MouseButtonEvent;
import org.jsfml.window.event.MouseEvent;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Vector;

public class CardWindow {
    public RenderWindow window;
    private View view;
    private Random rnd;
    private Vector2i windowPos;
    private Vector2f worldPos;
    private Vector2f screenSize;
    private Sprite pokerTable;
    private Clock frClock;
    private Conveyor conveyor;
    private Vector<Card> cards;
    private Vector<Animation> animations;
    private Vector<Vector2f> positions;
    private Vector<Player> players;

    public CardWindow(Conveyor conv) {
        this.conveyor = conv;
        init();
        initPlayersPoses(6);
        new Player(worldPos);
        for (Vector2f pos : positions) {
            players.add(new Player(pos));
        }
        System.out.println(view.getCenter()); // 500-500
        System.out.println(view.getViewport());// 0 0 1 1
        System.out.println(view.getSize());// 1000 1000

        // for (int i = 0; i != cards.size(); i++) {
        // animations.add(new Animation(cards.get(i), 3, new EaseOutQuart(), (card,
        // frame) -> {
        // card.getSprite().setRotation(frame * (float) (2 * 360));
        // }));
        // }
        // for (int i = 0; i!=cards.size();i++){
        // final int it = i;
        // animations.add(new Animation(cards.get(i),3,(time)->{return
        // (float)-(Math.cos(Math.PI * time)-1)/2;},(card,
        // frame)->{card.getSprite().setPosition(frame*(float)(5+it*10),frame*(float)(5+it*10));
        // }));
        // }
        RectangleShape startButton = new RectangleShape(new Vector2f(200, 75));
        RectangleShape skipButton = new RectangleShape(new Vector2f(200, 75));
        CircleShape currentPlayer = new CircleShape(15);
        startButton.setFillColor(Color.GREEN);
        skipButton.setFillColor(Color.GREEN);
        currentPlayer.setFillColor(Color.GREEN);
        startButton.setPosition(380, 715);
        skipButton.setPosition(20, 800);
        while (window.isOpen()) {
            Time dTime = frClock.restart();
            float dSeconds = dTime.asSeconds();
            window.clear(new Color(20, 120, 32, 255));
            window.draw(pokerTable);
            // for (int i = 0; i != 13 * 4; i++) {
            // cards.elementAt(i).draw(window);
            // }
            for (Player p : players) {
                p.drawCards(window);
            }
            window.draw(currentPlayer);
            window.draw(skipButton);
            window.draw(startButton);
            window.display();
            for (org.jsfml.window.event.Event event : window.pollEvents()) {
                switch (event.type) {
                    case CLOSED -> {
                        window.close();
                        return;
                    }
                    case KEY_PRESSED -> {
                        org.jsfml.window.event.KeyEvent keyEvent = event.asKeyEvent();
                        switch (keyEvent.key) {
                            case ESCAPE:
                                window.close();
                                break;
                            default:
                                break;
                        }
                    }
                    case MOUSE_BUTTON_PRESSED -> {
                        MouseButtonEvent keyEvent = event.asMouseButtonEvent();
                        switch (keyEvent.button) {
                            case LEFT:
                                windowPos = Mouse.getPosition(window);
                                worldPos = window.mapPixelToCoords(windowPos);
                                System.out.println(worldPos);
                                break;
                            default:
                                break;
                        }
                    }
                    default -> {
                        break;
                    }
                }
            }
        }
    }

    private void init() {
        initPokerTable();
        initCards();
        initWindowAndView();
        initAnimations();
    }

    private void initPokerTable() {
        try {
            Image imgloader;
            Texture greenBackground;
            imgloader = new Image();
            greenBackground = new Texture();
            imgloader.loadFromFile(Paths.get("Resources/PokerTable.png"));
            greenBackground.loadFromImage(imgloader);
            greenBackground.setRepeated(true);
            greenBackground.setSmooth(true);
            pokerTable = new Sprite(greenBackground);
            pokerTable.setTextureRect(new IntRect(0, 0, 1280, 960));
            pokerTable.setScale(1.0f, 1.0f);
        } catch (IOException | TextureCreationException e) {
            throw new RuntimeException(e);
        }
    }

    private void initCards() {
        try {
            Image imageCards;
            imageCards = new Image();
            imageCards.loadFromFile(Paths.get("Resources/Cards.png"));
            imageCards.createMaskFromColor(new Color(85, 170, 85, 255));
            Texture textureCards = new Texture();
            textureCards.loadFromImage(imageCards);
            Sprite sprite = new Sprite(textureCards);
            cards = new Vector<>();
            for (int i = 0; i != 13; i++) {
                for (int j = 0; j != 4; j++) {
                    sprite.setTextureRect(new IntRect(30 + 390 * i, 30 + 568 * j, 360, 538));
                    // sprite.setPosition(15 * i * 2 + 120 * j * 2, i * 57);
                    sprite.setScale(0.5f, 0.5f);
                    cards.add(new Card(sprite, CardNum.fromId(i), CardSuit.fromId(j)));
                    sprite = new Sprite(textureCards);
                }
            }
            imageCards = new Image();
            imageCards.loadFromFile(Paths.get("Resources/Card_back.png"));
            imageCards.createMaskFromColor(new Color(85, 170, 85, 255));
            textureCards = new Texture();
            textureCards.loadFromImage(imageCards);
            sprite.setTexture(textureCards);
            sprite.setTextureRect(new IntRect(84, 39, 738, 1029));
            sprite.setScale(0.248f, 0.265f);
            cards.add(new Card(sprite, CardNum.Ace, CardSuit.Clovers));
            // imageCards.createMaskFromColor(new Color(85, 170, 85, 255));
        } catch (IOException | TextureCreationException e) {
            throw new RuntimeException(e);
        }
    }

    private void initWindowAndView() {
        screenSize = this.getScreenSize();
        int size = (int) screenSize.x / 2;
        ContextSettings context = new ContextSettings(8, 8, 16);
        window = new RenderWindow(new VideoMode(size, size / 4 * 3), "Cards Online", WindowStyle.DEFAULT, context);
        window.setVerticalSyncEnabled(true);
        view = (View) window.getDefaultView();
        view.setSize(640 * 2, 480 * 2);
        view.setCenter(320 * 2, 240 * 2);
        window.setView(view);
    }

    private void initAnimations() {
        animations = new Vector<>();
        frClock = new Clock();
    }

    private Vector2f getScreenSize() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        Vector2f screenSize = new Vector2f(width, height);
        return screenSize;
    }

    private void initPlayersPoses(int count) {
        positions = new Vector<>();
        players = new Vector<Player>();
        for (int i = 0; i != count; i++) {
            double c = Math.cos((float) Math.PI * 2 / count * i + (float) Math.PI / 2);
            double s = Math.sin((float) Math.PI * 2 / count * i + (float) Math.PI / 2);
            Vector2f vecCen = view.getCenter();
            c = c * 30 * 15 + vecCen.x / 1.35;
            s = s * 30 * 10 + vecCen.y / 1.5;
            System.out.println(c + "  " + s);
            this.positions.add(new Vector2f((float) c, (float) s));
        }
    }

    private Card getCard(CardNum crdNum, CardSuit crdSuit) {
        return this.cards.elementAt(CardNum.fromName(crdNum) * 4 + CardSuit.fromName(crdSuit));
    }

    private Card getBackCard() {
        return this.cards.elementAt(cards.size() - 1);
    }
}