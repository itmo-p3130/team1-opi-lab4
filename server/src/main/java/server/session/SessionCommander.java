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
                Request newRequest = new Request(req.getInitialization());
                newRequest.setType(RequestConstants.GET_DATA_FROM_GAME_SESSION);
                newRequest.addData(RequestConstants.CURRENT_PLAYER, ses.getCurrentPlayer().getInitialization());
            }
            case SET_DATA_TO_GAME_SESSION -> {

            }
        }
    }
}