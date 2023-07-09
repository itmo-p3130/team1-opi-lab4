package client.cardWindow;

import org.jsfml.graphics.*;

public class Card extends Sprite {
    private CardSuit cardSuit;
    private CardNum cardNum;
    private Sprite sprite;
    Card(Sprite spr, CardNum num, CardSuit suit) {
        this.cardNum = num;
        this.sprite = spr;
        this.cardSuit = suit;
    }
    public void draw(RenderWindow window){
        window.draw(this.sprite);
    }
}
