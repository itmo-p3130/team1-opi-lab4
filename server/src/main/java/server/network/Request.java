package server.network;

import java.util.Collections;
import java.util.UUID;
import java.util.Vector;

public class Request {
    private final UUID initialization;
    private final String data;
    private final Vector<Integer> numberOfCards;
    // private final Vector<> cardsAtTower;

    public Request(UUID init, String content) {
        this.initialization = init;
        this.data = content;
        numberOfCards = new Vector<>();
    }

    public String getData() {
        return this.data;
    }

    public UUID getInitialization() {
        return this.initialization;
    }

    public void setNumberOfCards(Integer[] num) {
        Collections.addAll(numberOfCards, num);
    }

    public Vector<Integer> getNumberOfCards() {
        return numberOfCards;
    }
}
