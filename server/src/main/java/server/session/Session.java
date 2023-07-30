package server.session;

import java.util.Vector;

public class Session {
    private Vector<Integer> players;
    private Integer id;

    public Session(Integer ID) {
        this.players = new Vector<>();
        this.id = ID;
    }

}
