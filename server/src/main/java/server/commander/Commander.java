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
                    Request response = addFields(req.getInitialization(), RequestConstants.initGameSession,
                            RequestConstants.status, RequestConstants.failed, RequestConstants.reason,
                            "Could'n find your ID");
                    addResponse(response);
                    return;
                }
                Object gameSessionName = req.getData("--Game-Session-Name");
                String sessionName = "";
                if (gameSessionName instanceof String) {
                    sessionName = (String) gameSessionName;
                } else {
                    Request response = addFields(req.getInitialization(), RequestConstants.initGameSession,
                            RequestConstants.status, RequestConstants.failed, RequestConstants.reason,
                            "Couldn't parse game session name");
                    addResponse(response);
                    return;
                }
                if (conveyor.sessions.contains(sessionName)) {
                    Request response = addFields(req.getInitialization(), RequestConstants.initGameSession,
                            RequestConstants.status, RequestConstants.failed, RequestConstants.reason,
                            "There is already a session with that name");
                    addResponse(response);
                    return;
                }
                Session session = new Session(sessionName);
                session.addPlayer(player);
                conveyor.sessions.put(sessionName, session);
                Request response = addFields(req.getInitialization(), RequestConstants.initGameSession,
                        RequestConstants.status, RequestConstants.success);
                addResponse(response);
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

    private void addResponse(Request req) {
        conveyor.responses.add(req);
    }

    private Request addFields(UUID uid, String type, String... fields) {
        Request request = new Request(uid);
        request.setType(type);
        for (int i = 0; i <= fields.length - 1; i += 2) {
            request.addData(fields[i], fields[i + 1]);
        }
        return request;
    }
}
