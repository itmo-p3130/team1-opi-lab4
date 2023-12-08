package server.commander;

import java.util.UUID;

import server.confirmer.Confirmer;
import server.conveyor.Conveyor;
import server.network.Request;
import server.session.Session;
import server.user.User;

public class Commander extends Thread {
    private final Conveyor conveyor;
    private static final Integer PLAYERS_MAX = 6;

    public Commander(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void run() {
        while (Boolean.TRUE.equals(conveyor.isWorking)) {
            if (conveyor.requests.isEmpty()) {
                synchronized (conveyor.requests) {
                    try {
                        conveyor.requests.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                distributeRequest(conveyor.requests.get(0));
                conveyor.requests.remove(0);
                synchronized (conveyor.responses) {
                    conveyor.responses.notifyAll();
                }
                synchronized (conveyor.session.getRequests()) {
                    conveyor.session.getRequests().notifyAll();
                }
            }
        }
    }

    private void distributeRequest(Request req) {
        Object type = req.getType();
        switch ((RequestConstants) type) {
            // case INIT_GAME_SESSION -> {
            // initGameSession(req);
            // }
            case CONNECT_TO_GAME_SESSION -> {
                connectToGameSession(req);
            }
            case QUIT_FROM_GAME_SESSION -> {
                quitFromGameSession(req);
            }
            case SET_DATA_TO_GAME_SESSION -> {
                setDataToGameSession(req);
            }
            default -> {
            }
        }
    }

    private void addResponse(Request req) {
        conveyor.responses.add(req);
    }

    private Request addFields(Integer uid, RequestConstants type, Object... fields) {
        Request request = new Request(uid);
        request.setType(type);
        for (int i = 0; i <= fields.length - 1; i += 2) {
            request.addData(fields[i], fields[i + 1]);
        }
        return request;
    }



    private void connectToGameSession(Request req) {
        if (conveyor.session.getPlayers().size() >= PLAYERS_MAX) {
            Request response = addFields(req.getInitialization(), RequestConstants.CONNECT_TO_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    "There are too many players in this session");
            addResponse(response);
            return;
        }
        User player = new User(req.getInitialization(), conveyor.connections.get(req.getInitialization()));
        conveyor.session.addPlayer(player);
        if (conveyor.session.getPlayers().size() == 1) {
            conveyor.session.setOwner(player.getInitialization());
        }
        Request response = addFields(req.getInitialization(), RequestConstants.CONNECT_TO_GAME_SESSION,
                RequestConstants.STATUS, RequestConstants.SUCCESS);
        addResponse(response);
    }

    private void quitFromGameSession(Request req) {
        Integer uuid = req.getInitialization();
        User player = conveyor.clients.get(uuid);
        if (player == null) {
            Request response = addFields(req.getInitialization(), RequestConstants.QUIT_FROM_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    RequestConstants.COULDNT_FIND_YOUR_ID);
            addResponse(response);
            return;
        }
        Session session = player.getSession();
        if (session != null) {
            session.getPlayers().remove(player);
            session.setGameToOver("Player " + player.getInitialization() + " has left the game");
            Request response = addFields(req.getInitialization(), RequestConstants.QUIT_FROM_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.SUCCESS);
            addResponse(response);
        }
        player.setSession(null);
    }

    private void getDataFromGameSession(Request req) {
        sendRequestToGameSession(req, RequestConstants.GET_DATA_FROM_GAME_SESSION);
    }

    private void setDataToGameSession(Request req) {
        sendRequestToGameSession(req, RequestConstants.SET_DATA_TO_GAME_SESSION);
    }

    private void sendRequestToGameSession(Request req, RequestConstants reqConst) {
        Integer uuid = req.getInitialization();
        User player = conveyor.clients.get(uuid);
        conveyor.session.addRequest(req);
    }

    private void defaultRequestType(Request req) {
        Request response = addFields(req.getInitialization(), RequestConstants.ERROR_REQUEST,
                RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                RequestConstants.UNSUPPORTED_REQUEST_FORMAT);
        addResponse(response);

    }
}
