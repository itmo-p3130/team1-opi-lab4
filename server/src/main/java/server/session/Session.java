package server.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import server.conveyor.cards.Card;
import server.conveyor.cards.CardNum;
import server.conveyor.cards.CardSuit;
import server.conveyor.cards.CardsTower;
import server.network.Request;
import server.user.User;

public class Session {
    private Vector<User> players;
    private Vector<Request> requests;
    private Boolean isPlayNow;
    private Boolean isOver;
    private String reasonForOvering;
    private Integer countOfPlayers;
    private User playerTurn;
    private Card bottomCard;
    private Vector<Card> allCardsTower;
    private Vector<Card> turnCardsTower;
    private HashMap<UUID, Vector<Card>> playersCards;
    private String name;

    public Session(String name) {
        this.players = new Vector<>();
        this.name = name;
        this.isPlayNow = false;
        this.turnCardsTower = CardsTower.getTower();
        this.playersCards = new HashMap<>();
    }

    public void addPlayer(User user) {
        players.add(user);
        playersCards.put(user.getInitialization(), new Vector<Card>());
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

    public void setCurrentPlayer(UUID uid) {
        this.playerTurn = findPlayer(uid);
    }

    public User findPlayer(UUID uid) {
        for (User user : this.players) {
            if (user.getInitialization() == uid)
                return user;
        }
        return null;
    }

    public List<UUID> getPlayersList() {
        return players.stream()
                .map(User::getInitialization)
                .collect(Collectors.toList());
    }

    public Boolean isOver() {
        return this.isOver;
    }

    public void setGameToOver(String reason) {
        this.isOver = true;
        this.reasonForOvering = reason;
    }

    public Boolean isGameOver() {
        return this.isOver;
    }

    public String getReasonForGameOver() {
        return this.reasonForOvering;
    }

    public Integer getTotalCardsNumber() {
        return this.allCardsTower.size();
    }

    public Vector<Card> getTurnCardsTower() {
        return this.turnCardsTower;
    }

    public Vector<Card> getPlayerCards(UUID uid) {
        return this.playersCards.get(uid);
    }

    public HashMap<UUID, Card> getAllPlayersCards(UUID uid) {
        HashMap<UUID, Card> playersCards = (HashMap<UUID, Card>) this.playersCards.clone();
        playersCards.remove(uid);
        return playersCards;
    }
}
