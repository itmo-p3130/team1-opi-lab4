package client.cardWindow.Players;

import java.util.Vector;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

import client.cardWindow.Cards.Card;

public class Player {
    private Vector2f position;
    private Vector<Card> cards;
    private Vector2f defaultSize;
    private int selectedCard;

    public Player(Vector2f pos) {
        this.position = pos;
        this.cards = new Vector<Card>();
    }

    public void mouseAtCard(int card) {
        defaultSize = cards.elementAt(card).getSprite().getScale();
        cards.elementAt(card).getSprite().setScale(defaultSize.x * 1.5f, defaultSize.y * 1.5f);
        cards.elementAt(selectedCard).getSprite().setScale(defaultSize);
        selectedCard = card;
    }

    public void drawCards(RenderWindow win) {
        for (Card card : cards) {
            card.draw(win);
        }
    }

    public void setSize(float fl) {
        for (Card card : cards) {
            card.getSprite().setScale(fl, fl);
        }
    }

    public void addCard(Card card) {
        Card nCard = new Card(card);
        nCard.getSprite().setPosition(position.x + 40 * cards.size(), position.y);
        cards.add(nCard);
        for (Card c : cards) {
            System.out.println(c.getSprite().getPosition());
            System.out.println();
        }
    }

    public void throwCard(int card) {
        cards.remove(card);
    }
}
