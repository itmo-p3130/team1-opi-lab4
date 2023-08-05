package server.commander;

import java.util.Set;
import java.util.UUID;

import server.confirmer.Confirmer;
import server.conveyor.Conveyor;
import server.network.Request;
import server.session.Session;
import server.user.User;

public class Commander extends Thread {
    private final Conveyor conveyor;

    public Commander(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void run() {
        while (Boolean.TRUE.equals(conveyor.isWorking)) {
            if (conveyor.requests.isEmpty()) {
                synchronized (conveyor) {
                    try {
                        conveyor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                distributeRequest(conveyor.requests.get(0));
            }
        }
    }

    private void distributeRequest(Request req) {
        String type = req.getType();
        switch (type) {
            case RequestConstants.uuidRegistration -> {
                UUID uid = Confirmer.getToken();
                User user = new User(uid, req.getConnection());
                conveyor.connections.put(req.getConnection(), uid);
                conveyor.clients.put(uid, user);
            }
            case RequestConstants.initGameSession -> {
                UUID uuid = req.getInitialization();
                User player = conveyor.clients.get(uuid);
                if (player == null) {
                    Request response = new Request(req.getInitialization());
                    response.setType(RequestConstants.initGameSession);
                    response.setData(RequestConstants.status, RequestConstants.failed);
                    response.setData(RequestConstants.reason, "Could'n find your ID");
                    conveyor.responses.add(response);
                    return;
                }
                Object gameSessionName = req.getData("--Game-Session-Name");
                String sessionName = "";
                if (gameSessionName instanceof String) {
                    sessionName = (String) gameSessionName;
                } else {
                    Request response = new Request(req.getInitialization());
                    response.setType(RequestConstants.initGameSession);
                    response.setData(RequestConstants.status, RequestConstants.failed);
                    response.setData(RequestConstants.reason, "Couldn't parse game session name");
                    conveyor.responses.add(response);
                    return;
                }
                if (conveyor.sessions.contains(sessionName)) {
                    Request response = new Request(req.getInitialization());
                    response.setType(RequestConstants.initGameSession);
                    response.setData(RequestConstants.status, RequestConstants.failed);
                    response.setData(RequestConstants.reason, "There is already a session with that name");
                    conveyor.responses.add(response);
                    return;
                }
                Session session = new Session(sessionName);
                session.addPlayer(player);
                conveyor.sessions.put(sessionName, session);
                Request response = new Request(req.getInitialization());
                response.setType(RequestConstants.initGameSession);
                response.setData(RequestConstants.status, RequestConstants.succes);
                conveyor.responses.add(response);
            }
            case RequestConstants.connectToGameSession -> {

            }
            case RequestConstants.quitFromGameSession -> {

            }
            case RequestConstants.getDataFromGameSession -> {

            }
            case RequestConstants.setDataToGameSession -> {

            }
            default -> {

            }
        }
    }
}
