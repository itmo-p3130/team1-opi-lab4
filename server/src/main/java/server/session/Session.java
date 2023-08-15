package server.session;

import java.util.Vector;

import server.conveyor.Cards.Card;
import server.conveyor.Cards.CardNum;
import server.conveyor.Cards.CardSuit;
import server.network.Request;
import server.user.User;

public class Session {
    private Vector<User> players;
    private Vector<Request> requests;
    private Boolean isPlayNow;
    private Integer countOfPlayers;
    private User playerTurn;
    private Card bottomCard;
    private String name;

    public Session(String name) {
        this.players = new Vector<>();
        this.name = name;
        this.isPlayNow = false;
    }

    public void addPlayer(User user) {
        players.add(user);
    }

    public Vector<User> getPlayers() {
        return this.players;
    }

    public String getName() {
        return this.name;
    }

    public void addRequest(Request req) {
        requests.add(req);
    }

    public Vector<Request> getRequests() {
        return this.requests;
    }

    public Boolean isPlayNow() {
        return this.isPlayNow;
    }

    public void setIsPlayNow(boolean mk) {
        this.isPlayNow = mk;
    }

    public Card getBottomCard() {
        return this.bottomCard;
    }

    public void setBottomCard(Card card) {
        this.bottomCard = card;
    }

    public void setBottomCard(CardNum num, CardSuit suit) {
        this.bottomCard = new Card(num, suit);
    }

    public User getCurrentPlayer() {
        return this.playerTurn;
    }
}
