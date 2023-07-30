package server.conveyor;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryonet.Client;

import server.session.Session;

public class Conveyor {
    public final ConcurrentHashMap<Integer, Client> clients;
    public final ConcurrentHashMap<Integer, Session> sessions;
    public final Vector<Request> requests;

    public Conveyor() {
        this.clients = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
        this.requests = new Vector<>();
    }
}
