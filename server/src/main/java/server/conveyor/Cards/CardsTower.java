package server.conveyor.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class CardsTower {
    private CardsTower() {
    }

    public static Vector<Card> getTower() {
        ArrayList<Card> cards = generatePairs();
        return new Vector<>(cards);
    }

    private static ArrayList<Card> generatePairs() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i <= 12; i++) {
            for (int j = 0; j <= 3; j++) {
                cards.add(new Card(CardNum.fromId(i), CardSuit.fromId(j)));
            }
        }
        Collections.shuffle(cards);
        return cards;
    }
}
