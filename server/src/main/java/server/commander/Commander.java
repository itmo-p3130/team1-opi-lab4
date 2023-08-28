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
                conveyor.responses.notifyAll();
            }
        }
    }

    private void distributeRequest(Request req) {
        Object type = req.getType();
        switch ((RequestConstants) type) {
            case UUID_REGISTRATION -> {
                uuidRegistration(req);
            }
            case INIT_GAME_SESSION -> {
                initGameSession(req);
            }
            case CONNECT_TO_GAME_SESSION -> {
                connectToGameSession(req);
            }
            case QUIT_FROM_GAME_SESSION -> {
                quitFromGameSession(req);
            }
            case GET_DATA_FROM_GAME_SESSION -> {
                getDataFromGameSession(req);
            }
            case SET_DATA_TO_GAME_SESSION -> {
                setDataToGameSession(req);
            }
            default -> {
                defaultRequestType(req);
            }
        }
    }

    private void addResponse(Request req) {
        conveyor.responses.add(req);
    }

    private Request addFields(String uid, RequestConstants type, Object... fields) {
        Request request = new Request(uid);
        request.setType(type);
        for (int i = 0; i <= fields.length - 1; i += 2) {
            request.addData(fields[i], fields[i + 1]);
        }
        return request;
    }

    private void uuidRegistration(Request req) {
        String uid = Confirmer.getToken().toString();
        User user = new User(uid, req.getConnection());
        conveyor.connections.put(req.getConnection(), uid);
        conveyor.clients.put(uid, user);
        Request newReq = new Request(uid);
        newReq.setType(RequestConstants.UUID_REGISTRATION);
        newReq.addData(RequestConstants.UUID_REGISTRATION, uid);
        addResponse(newReq);
    }

    private void initGameSession(Request req) {
        String uuid = req.getInitialization();
        User player = conveyor.clients.get(uuid);
        if (player == null) {
            Request response = addFields(req.getInitialization(), RequestConstants.INIT_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    RequestConstants.COULDNT_FIND_YOUR_ID);
            addResponse(response);
            return;
        }
        Object gameSessionName = req.getData(RequestConstants.GAME_SESSION_NAME);
        String sessionName = "";
        if (gameSessionName instanceof String) {
            sessionName = (String) gameSessionName;
        } else {
            Request response = addFields(req.getInitialization(), RequestConstants.INIT_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    "Couldn't parse game session name");
            addResponse(response);
            return;
        }
        if (conveyor.sessions.contains(sessionName)) {
            Request response = addFields(req.getInitialization(), RequestConstants.INIT_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    "There is already a session with that name");
            addResponse(response);
            return;
        }
        Session session = new Session(sessionName, req.getInitialization());
        session.addPlayer(player);
        conveyor.sessions.put(sessionName, session);
        conveyor.sessions.notifyAll();
        Request response = addFields(req.getInitialization(), RequestConstants.INIT_GAME_SESSION,
                RequestConstants.STATUS, RequestConstants.SUCCESS);
        addResponse(response);
    }

    private void connectToGameSession(Request req) {
        String uuid = req.getInitialization();
        User player = conveyor.clients.get(uuid);
        if (player == null) {
            Request response = addFields(req.getInitialization(), RequestConstants.CONNECT_TO_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    RequestConstants.COULDNT_FIND_YOUR_ID);
            addResponse(response);
            return;
        }
        Object gameSessionName = req.getData(RequestConstants.GAME_SESSION_NAME);
        String sessionName = "";
        if (gameSessionName instanceof String) {
            sessionName = (String) gameSessionName;
        } else {
            Request response = addFields(req.getInitialization(), RequestConstants.CONNECT_TO_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    "Couldn't parse game session name");
            addResponse(response);
            return;
        }
        Session session = conveyor.sessions.get(sessionName);
        if (session == null) {
            Request response = addFields(req.getInitialization(), RequestConstants.CONNECT_TO_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    "There is no session with that name");
            addResponse(response);
            return;
        } else if (session.getPlayers().size() >= PLAYERS_MAX) {
            Request response = addFields(req.getInitialization(), RequestConstants.CONNECT_TO_GAME_SESSION,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    "There are too many players in this session");
            addResponse(response);
            return;
        }
        session.addPlayer(player);
        Request response = addFields(req.getInitialization(), RequestConstants.CONNECT_TO_GAME_SESSION,
                RequestConstants.STATUS, RequestConstants.SUCCESS);
        addResponse(response);
    }

    private void quitFromGameSession(Request req) {
        String uuid = req.getInitialization();
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
            if (session.getPlayers().isEmpty()) {
                conveyor.sessions.remove(session.getName());
            }
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
        String uuid = req.getInitialization();
        User player = conveyor.clients.get(uuid);
        if (player == null) {
            Request response = addFields(req.getInitialization(), reqConst,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    RequestConstants.COULDNT_FIND_YOUR_ID);
            addResponse(response);
            return;
        }
        Session session = player.getSession();
        if (session == null) {
            Request response = addFields(req.getInitialization(), reqConst,
                    RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                    "This session doesn't exist");
            addResponse(response);
            return;
        }
        session.addRequest(req);
    }

    private void defaultRequestType(Request req) {
        Request response = addFields(req.getInitialization(), RequestConstants.ERROR_REQUEST,
                RequestConstants.STATUS, RequestConstants.FAILED, RequestConstants.REASON,
                RequestConstants.UNSUPPORTED_REQUEST_FORMAT);
        addResponse(response);

    }
}
