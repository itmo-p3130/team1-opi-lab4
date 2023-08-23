package server.session;

import java.util.HashMap;

import com.esotericsoftware.kryonet.Connection;

import server.conveyor.Conveyor;
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
                newRequest.addData(RequestConstants.ALL_PLAYERS_CARDS, ses.getAllPlayersCards(req.getInitialization()));
                connection.sendTCP(newRequest);
            }
            case SET_DATA_TO_GAME_SESSION -> {

            }
            default -> {
                Request newRequest;
                newRequest = new Request(req.getInitialization());
                connection.sendTCP(newRequest);
            }
        }

    }
}