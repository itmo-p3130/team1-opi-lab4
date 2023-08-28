package server.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

import server.commander.RequestConstants;
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
    private User playerAttack;
    private Card bottomCard;
    private Vector<Card> allCardsTower;
    private Vector<Card> turnCardsTower;
    private HashMap<Integer, Vector<Card>> playersCards;
    private String name;
    private Integer sessionOwner;

    public Session(String name, Integer owner) {
        this.players = new Vector<>();
        this.name = name;
        this.isPlayNow = false;
        this.allCardsTower = CardsTower.getTower();
        this.bottomCard = this.allCardsTower.lastElement();
        this.playersCards = new HashMap<>();
        this.sessionOwner = owner;
        this.requests = new Vector<>();
        this.isOver = false;
    }

    public void addPlayer(User user) {
        players.add(user);
        playersCards.put(user.getInitialization(), new Vector<Card>());
    }

    public void setOwner(Integer uid) {
        this.sessionOwner = uid;
    }

    public Vector<User> getPlayers() {
        return this.players;
    }

    public void logicSetGameStarts() {
        this.countOfPlayers = this.players.size();
        setIsPlayNow(true);
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

    public void setCurrentPlayer(Integer uid) {
        this.playerTurn = findPlayer(uid);
    }

    public User findPlayer(Integer uid) {
        for (User user : this.players) {
            if (user.getInitialization() == uid)
                return user;
        }
        return null;
    }

    public List<Integer> getPlayersList() {
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
        this.isPlayNow = false;
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

    public Vector<Card> getPlayerCards(Integer uid) {
        return this.playersCards.get(uid);
    }

    public HashMap<Integer, Integer> getAllPlayersCards() {
        HashMap<Integer, Integer> playersCards = new HashMap<>();
        for (User user : this.players) {
            playersCards.put(user.getInitialization(), this.playersCards.get(user.getInitialization()).size());
        }
        return playersCards;
    }

    public Boolean logicCanAddCardToTower(Card card, Integer player) {
        if (this.playerTurn.getInitialization() == player) {
            Card lastCard = this.turnCardsTower.lastElement();
            if (lastCard == null) {
                this.turnCardsTower.add(card);
                this.playerAttack = this.findPlayer(player);
                return true;
            } else if (this.turnCardsTower.size() % 2 == 1) {
                int num = (lastCard.getNum().id + 12) % 13;
                if (lastCard.getSuit() == this.bottomCard.getSuit()) {
                    num *= 10;
                }
                int numNew = (card.getNum().id + 12) % 13;
                if (card.getSuit() == this.bottomCard.getSuit()) {
                    numNew *= 10;
                }
                if (card.getSuit() == lastCard.getSuit() || card.getSuit() == this.bottomCard.getSuit()) {
                    if (numNew > num) {
                        turnCardsTower.add(card);
                        return true;
                    }
                }

            } else {
                for (Card crd : turnCardsTower) {
                    if (crd.getNum() == card.getNum()) {
                        turnCardsTower.add(card);
                        return true;
                    }
                }
                return false;

            }
        }
        return false;
    }

    public void logicPlayerTakeTower(Integer user) {
        this.playersCards.get(user).addAll(this.turnCardsTower);
    }

    public void logicTowerTurnEnd() {
        this.allCardsTower = new Vector<>();
        this.playerAttack = null;
    }

    public void logicGiveCardsToAllPlayers() {
        for (User user : this.players) {
            while (this.playersCards.get(user.getInitialization()).size() < 6 && !this.allCardsTower.isEmpty()) {
                this.playersCards.get(user.getInitialization()).add(this.allCardsTower.firstElement());
            }
        }
    }

    public void logicChoseFirstPlayer() {
        Random rnd = new Random();
        this.playerTurn = this.players.get(rnd.nextInt(this.countOfPlayers));
    }

    public Integer logicCheckIsThereWinner() {
        for (User user : this.players) {
            if (playersCards.get(user.getInitialization()).isEmpty() && allCardsTower.isEmpty()) {
                return user.getInitialization();
            }
        }
        return null;
    }

    public User logicTurnToNextPlayer() {
        this.playerTurn = this.players.get((this.players.indexOf(this.playerTurn) + 1) % this.players.size());
        return this.playerTurn;
    }

    public User logicTurnToPreviousPlayer() {
        this.playerTurn = this.players
                .get((this.players.indexOf(this.playerTurn) + this.players.size() - 1) % this.players.size());
        return this.playerTurn;
    }

    public void utilSendToAllExcept(UUID uid, Object packet) {
        // SEND TO ALL USERS WITH FOR-EACH
    }

    public Vector<Request> utilSendToAllCurrentGameSessionData() {
        Vector<Request> reqs = new Vector<>();
        for (User user : this.players) {
            Request newRequest;
            newRequest = new Request(user.getInitialization());
            newRequest.setType(RequestConstants.SET_GAME_START);
            newRequest.addData(RequestConstants.PLAYERS_IN_SESSION, this.getPlayersList());
            newRequest.addData(RequestConstants.CURRENT_PLAYER, this.getCurrentPlayer().getInitialization());
            newRequest.addData(RequestConstants.CARDS_AT_PLAYER, this.getPlayerCards(user.getInitialization()));
            newRequest.addData(RequestConstants.CARDS_IN_TOWER, this.getTurnCardsTower());
            newRequest.addData(RequestConstants.BOTTOM_CARD, this.getBottomCard());
            newRequest.addData(RequestConstants.TOTAL_CARDS_NUMBER, this.getTotalCardsNumber());
            newRequest.addData(RequestConstants.ALL_PLAYERS_CARDS, this.getAllPlayersCards());
            newRequest.addData(RequestConstants.IS_GAME_PLAYING, this.isPlayNow);
            reqs.add(newRequest);
        }

        return reqs;
    }

    public Integer getOwner() {
        return this.sessionOwner;
    }

    public Integer getAttackPlayer() {
        return this.playerAttack.getInitialization();
    }
}
