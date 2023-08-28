package client.cardWindow.Cards;

import org.jsfml.graphics.*;

public class Card {
    private CardSuit cardSuit;
    private CardNum cardNum;
    private transient Sprite sprite;

    public Card(Sprite spr, CardNum num, CardSuit suit) {
        this.cardNum = num;
        this.sprite = spr;
        this.cardSuit = suit;
    }

    public Card(Card card) {
        this.cardNum = CardNum.fromId(card.cardNum.id);
        this.cardSuit = CardSuit.fromId(card.cardSuit.id);
        this.sprite = new Sprite(card.getSprite().getTexture());
        this.sprite.setScale(card.getSprite().getScale());
    }

    public Card() {

    }

    public void draw(RenderWindow window) {
        window.draw(this.sprite);
    }

    public Sprite getSprite() {
        return this.sprite;
    }
}
