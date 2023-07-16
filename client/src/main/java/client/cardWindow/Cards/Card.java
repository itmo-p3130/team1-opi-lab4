package client.cardWindow.Cards;

import org.jsfml.graphics.*;

public class Card {
    private CardSuit cardSuit;
    private CardNum cardNum;
    private Sprite sprite;
    public Card(Sprite spr, CardNum num, CardSuit suit) {
        this.cardNum = num;
        this.sprite = spr;
        this.cardSuit = suit;
    }
    public void draw(RenderWindow window){
        window.draw(this.sprite);
    }
    public Sprite getSprite(){
        return this.sprite;
    }
}
