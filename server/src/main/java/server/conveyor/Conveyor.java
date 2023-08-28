package server.conveyor;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryonet.Connection;

import server.user.User;
import server.network.Request;
import server.session.Session;

public class Conveyor {
    public final ConcurrentHashMap<String, User> clients;
    public final ConcurrentHashMap<String, Session> sessions;
    public final ConcurrentHashMap<Connection, String> connections;
    public final Vector<Request> requests;
    public final Vector<Request> responses;
    public Boolean isWorking;

    public Conveyor() {
        this.clients = new ConcurrentHashMap<>();
        this.sessions = new ConcurrentHashMap<>();
        this.connections = new ConcurrentHashMap<>();
        this.requests = new Vector<>();
        this.responses = new Vector<>();
        this.isWorking = true;
    }
}
