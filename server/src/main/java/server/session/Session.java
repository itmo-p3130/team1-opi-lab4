package server.session;

import java.util.Vector;

import server.user.User;

public class Session {
    private Vector<User> players;
    private String name;

    public Session(String name) {
        this.players = new Vector<>();
        this.name = name;
    }

    public void addPlayer(User user) {
        players.add(user);
    }

    public String getName() {
        return this.name;
    }

}
