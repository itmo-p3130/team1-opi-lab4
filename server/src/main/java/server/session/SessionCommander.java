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
            if (conveyor.session.getRequests().isEmpty()) {
                synchronized (conveyor.session.getRequests()) {
                    try {
                        conveyor.session.getRequests().wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                handleRequest(conveyor.session.getRequests().firstElement(), conveyor.session);
                conveyor.session.getRequests().remove(0);
            }
        }
    }

    private void handleRequest(Request req, Session ses) {
        Connection connection = req.getConnection();
        HashMap<Object, Object> data = req.getData();
        Object type = req.getType();
        switch ((RequestConstants) type) {
            case SET_DATA_TO_GAME_SESSION -> {
                HashMap<Object, Object> fields = req.getData();
                Integer uidOBJ = (Integer) fields.get(RequestConstants.SET_GAME_START);
                if (uidOBJ instanceof Integer) {// CHECKING FOR STARTING
                    Integer uid = (Integer) uidOBJ;
                    System.err.println(ses.getOwner() + " " + uid);
                    if (uid == ses.getOwner() || conveyor.connections.size() == 1) {
                        ses.logicSetGameStarts();
                        ses.logicGiveCardsToAllPlayers();
                        ses.logicChoseFirstPlayer();
                        conveyor.responses.addAll(ses.utilSendToAllCurrentGameSessionData());
                        System.err.println("Session has been started");
                        System.err.println("People in session: " + ses.getPlayers().size());
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
                        conveyor.responses.addAll(ses.utilSendToAllCurrentGameSessionData());
                        return;
                    }
                    Object endTowerTurnOBJ = req.getData(RequestConstants.TURN_END);
                    if (ses.getCurrentPlayer().getInitialization() == req.getInitialization()) {// CHECKING FOR ENDING
                                                                                                // OF FIGHT
                        if (endTowerTurnOBJ instanceof String) {
                            if (ses.getCurrentPlayer().getInitialization() == req.getInitialization()
                                    && ses.getAttackPlayer() == (Integer) endTowerTurnOBJ
                                    && ses.getTurnCardsTower().size() >= 2) {

                                ses.logicTowerTurnEnd();
                                ses.logicGiveCardsToAllPlayers();
                                Integer uid = ses.logicCheckIsThereWinner();
                                ses.logicTurnToNextPlayer();
                                if (uid != null) {
                                    ses.setGameToOver("The winner is " + uid);
                                }
                            }
                        }
                    } else {
                        if (endTowerTurnOBJ instanceof String) {
                            if (ses.getCurrentPlayer().getInitialization() == (Integer) endTowerTurnOBJ) {
                                ses.logicPlayerTakeTower(req.getInitialization());
                                ses.logicTowerTurnEnd();
                                ses.logicGiveCardsToAllPlayers();
                                Integer uid = ses.logicCheckIsThereWinner();
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
                System.err.println("Default block");
                conveyor.responses.add(newRequest);
            }
        }
        synchronized (conveyor.responses) {
            conveyor.responses.notifyAll();
        }

    }
}