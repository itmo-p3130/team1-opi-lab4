package server.session;

import java.util.Vector;

import server.network.Request;
import server.user.User;

public class Session {
    private Vector<User> players;
    private Vector<Request> requests;
    private String name;

    public Session(String name) {
        this.players = new Vector<>();
        this.name = name;
    }

    public void addPlayer(User user) {
        players.add(user);
    }

    public Vector<User> getPlayers() {
        return this.players;
    }

    public String getName() {
        return this.name;
    }

    public void addRequest(Request req) {
        requests.add(req);
    }

    public Vector<Request> getRequests() {
        return this.requests;
    }

}
