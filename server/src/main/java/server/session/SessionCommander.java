package server.session;

import java.util.HashMap;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import server.conveyor.Conveyor;
import server.conveyor.cards.Card;
import server.conveyor.cards.CardNum;
import server.conveyor.cards.CardSuit;
import server.network.Request;
import server.commander.RequestConstants;

public class SessionCommander extends Thread {
    private Conveyor conveyor;

    public SessionCommander(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void run() {
        while (conveyor.isWorking) {
            if (conveyor.sessions.isEmpty()) {
                synchronized (conveyor.sessions) {
                    try {
                        conveyor.sessions.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                dodgeSessions();
            }
        }
    }

    private void dodgeSessions() {
        conveyor.sessions.forEach(this::handleSession);
    }

    private void handleSession(String name, Session session) {

    }

    private void handleRequest(Request req, Session ses) {
        Connection connection = req.getConnection();
        HashMap<Object, Object> data = req.getData();
        Object type = req.getType();
        switch ((RequestConstants) type) {
            case GET_DATA_FROM_GAME_SESSION -> {
                Request newRequest;
                newRequest = new Request(req.getInitialization());
                newRequest.setType(RequestConstants.GET_DATA_FROM_GAME_SESSION);
                newRequest.addData(RequestConstants.PLAYERS_IN_SESSION, ses.getPlayersList());
                newRequest.addData(RequestConstants.CURRENT_PLAYER, ses.getCurrentPlayer().getInitialization());
                newRequest.addData(RequestConstants.CARDS_AT_PLAYER, ses.getPlayerCards(req.getInitialization()));
                newRequest.addData(RequestConstants.CARDS_IN_TOWER, ses.getTurnCardsTower());
                newRequest.addData(RequestConstants.BOTTOM_CARD, ses.getBottomCard());
                newRequest.addData(RequestConstants.TOTAL_CARDS_NUMBER, ses.getTotalCardsNumber());
                newRequest.addData(RequestConstants.ALL_PLAYERS_CARDS, ses.getAllPlayersCards());
                conveyor.responses.add(newRequest);
            }
            case SET_DATA_TO_GAME_SESSION -> {
                HashMap<Object, Object> fields = req.getData();
                Object uidOBJ = fields.get(RequestConstants.SET_GAME_START);
                if (uidOBJ instanceof UUID) {// CHECKING FOR STARTING
                    UUID uid = (UUID) uidOBJ;
                    if (uid == ses.getOwner()) {
                        ses.logicSetGameStarts();
                        ses.logicGiveCardsToAllPlayers();
                        ses.logicChoseFirstPlayer();
                        conveyor.responses.addAll(ses.utilSendToAllCurrentGameSessionData());
                    }
                }
                if (ses.isPlayNow()) {// FIGHT WITH SAME TOWER
                    Object cardNumOBJ = req.getData(RequestConstants.TURN_CARD_NUM);
                    Object cardSuitOBJ = req.getData(RequestConstants.TURN_CARD_SUIT);
                    if (cardNumOBJ instanceof CardNum && cardSuitOBJ instanceof CardSuit
                            && ses.getCurrentPlayer().getInitialization() == req.getInitialization()) {
                        Card crd = new Card((CardNum) cardNumOBJ, (CardSuit) cardSuitOBJ);
                        Request newRequest = new Request(req.getInitialization());
                        newRequest.setType(RequestConstants.SET_DATA_TO_GAME_SESSION);
                        if (ses.logicCanAddCardToTower(crd, req.getInitialization())) {
                            newRequest.addData(RequestConstants.STATUS, RequestConstants.SUCCESS);
                            if (ses.getTurnCardsTower().size() % 2 == 0) {
                                ses.logicTurnToPreviousPlayer();
                            } else {
                                ses.logicTurnToNextPlayer();
                            }
                            newRequest.addData(RequestConstants.CURRENT_PLAYER, ses.getCurrentPlayer());
                        } else {
                            newRequest.addData(RequestConstants.STATUS, RequestConstants.FAILED);
                        }
                        conveyor.responses.add(newRequest);
                        return;
                    }
                    Object endTowerTurnOBJ = req.getData(RequestConstants.TURN_END);
                    if (ses.getAttackPlayer() == req.getInitialization()) {// CHECKING FOR ENDING OF FIGHT
                        if (endTowerTurnOBJ instanceof UUID) {
                            if (ses.getCurrentPlayer().getInitialization() == req.getInitialization()
                                    && ses.getAttackPlayer() == (UUID) endTowerTurnOBJ
                                    && ses.getTurnCardsTower().size() >= 2) {

                                ses.logicTowerTurnEnd();
                                ses.logicGiveCardsToAllPlayers();
                                UUID uid = ses.logicCheckIsThereWinner();
                                ses.logicTurnToNextPlayer();
                                if (uid != null) {
                                    ses.setGameToOver("The winner is " + uid);
                                }
                            }
                        }
                    } else {
                        if (endTowerTurnOBJ instanceof UUID) {
                            if (ses.getCurrentPlayer().getInitialization() == (UUID) endTowerTurnOBJ) {
                                ses.logicPlayerTakeTower(req.getInitialization());
                                ses.logicTowerTurnEnd();
                                ses.logicGiveCardsToAllPlayers();
                                UUID uid = ses.logicCheckIsThereWinner();
                                if (uid != null) {
                                    ses.setGameToOver("The winner is " + uid);
                                }
                                ses.logicTurnToNextPlayer();
                            }
                        }

                    }
                    if (ses.isOver()) {
                        conveyor.responses.addAll(ses.utilSendToAllCurrentGameSessionData());
                    }
                }
            }
            default -> {
                Request newRequest;
                newRequest = new Request(req.getInitialization());
                conveyor.responses.add(newRequest);
            }
        }
        conveyor.responses.notifyAll();

    }
}