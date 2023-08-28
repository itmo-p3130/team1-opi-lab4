package client.conveyor;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import client.cardWindow.Cards.Card;

public class Commander extends Thread {
    private Conveyor conveyor;

    public Commander(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void run() {
        while (conveyor.client.isConnected()) {
            if (conveyor.requests.isEmpty()) {
                synchronized (conveyor.requests) {
                    try {
                        conveyor.requests.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                command();
            }
        }
    }

    public void command() {
        while (!conveyor.requests.isEmpty()) {
            Request req = conveyor.requests.firstElement();
            HashMap<Object, Object> mapData = req.getData();
            System.err.println("command solver");
            for (Map.Entry<Object, Object> entry : mapData.entrySet()) {
                RequestConstants rq = (RequestConstants) entry.getKey();
                switch (rq) {
                    case IS_GAME_PLAYING -> {
                        if ((Boolean) entry.getValue() && !conveyor.started) {
                            conveyor.started = true;
                            System.err.println("The game started");
                        }
                    }
                    case ALL_PLAYERS_CARDS -> {
                        if (entry.getValue() instanceof HashMap<?, ?>) {
                            HashMap<Integer, Integer> hshmap = (HashMap<Integer, Integer>) entry.getValue();
                            conveyor.clients.clear();
                            for (Map.Entry<Integer, Integer> e : hshmap.entrySet()) {
                                Integer playerName = e.getKey();
                                Integer cardValue = e.getValue();
                                if (playerName.equals(conveyor.userID)) {
                                    conveyor.clients.put(playerName, cardValue);
                                }
                            }
                        }
                    }
                    case BOTTOM_CARD -> {
                        conveyor.bottomCard = (Card) entry.getValue();
                        System.err.println("BOTTOM_CARD: " + conveyor.bottomCard);
                    }
                    case TOTAL_CARDS_NUMBER -> {
                        if ((Integer) entry.getValue() > 1) {
                            conveyor.isMoreCards = true;
                        }
                    }
                    case CARDS_IN_TOWER -> {
                        conveyor.cardsInTower = ((Vector<Card>) entry.getValue());
                    }
                    case CURRENT_PLAYER -> {
                        if (entry.getValue() instanceof Integer) {
                            Integer tmpUser = (Integer) entry.getValue();
                            conveyor.currentPlayer = (tmpUser);
                            System.err.println("CURRENT_PLAYER: " + conveyor.currentPlayer);
                        } else {
                            User tmpUser = (User) entry.getValue();
                            conveyor.currentPlayer = (tmpUser).getInitialization();
                            System.err.println("CURRENT_PLAYER: " + conveyor.currentPlayer);

                        }
                    }
                    case CARDS_AT_PLAYER -> {
                        conveyor.playerCards = (Vector<Card>) entry.getValue();
                        System.err.println("CARDS_AT_PLAYER: " + conveyor.playerCards);

                    }
                }
            }
            // switch (rq) {
            // case SET_GAME_START -> {
            // conveyor.started = true;
            // System.err.println("The game started");
            // HashMap<Object, Object> mapData = req.getData();
            // }
            // case GET_DATA_FROM_GAME_SESSION -> {
            // System.err.println("get data");
            // HashMap<Object, Object> mapData = req.getData();
            // }
            // case UUID_REGISTRATION -> {
            // conveyor.userID = req.getInitialization();
            // System.err.println("CURRENT UUID TYPE: " + req.getInitialization());

            // }
            // }
            conveyor.requests.remove(req);
        }
    }
}
