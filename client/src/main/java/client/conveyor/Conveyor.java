package client.conveyor;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryonet.Client;

import client.cardWindow.Cards.Card;
import client.cardWindow.Cards.CardNum;
import client.cardWindow.Cards.CardSuit;

public class Conveyor {
    public final ConcurrentHashMap<Integer, Integer> clients;
    public final Vector<Request> requests;
    public final Vector<Request> responses;
    public final Client client;
    public Vector<Card> cardsInTower;
    public Vector<Card> playerCards;
    public Integer userID;
    public boolean started;
    public Card bottomCard;
    public boolean isMoreCards;
    public Integer currentPlayer;

    public Conveyor() {
        this.requests = new Vector<>();
        this.responses = new Vector<>();
        this.clients = new ConcurrentHashMap<>();
        this.client = new Client();
        this.userID = -1;
        this.started = false;
        this.bottomCard = new Card();
        this.isMoreCards = true;
        this.playerCards = new Vector<>();
        this.cardsInTower = new Vector<>();
        this.currentPlayer = -1;
    }
}
