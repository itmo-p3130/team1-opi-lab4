package server.conveyor.cards;

public class Card {
    private CardSuit cardSuit;
    private CardNum cardNum;

    public Card(CardNum num, CardSuit suit) {
        this.cardNum = num;
        this.cardSuit = suit;
    }

    public Card(Card card) {
        this.cardNum = CardNum.fromId(card.cardNum.id);
        this.cardSuit = CardSuit.fromId(card.cardSuit.id);
    }

    @Override
    public String toString() {
        return cardNum.name() + " " + cardSuit.name();
    }
}
