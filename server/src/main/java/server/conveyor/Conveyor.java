package server.conveyor;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryonet.Connection;

import server.user.User;
import server.network.Request;
import server.session.Session;

public class Conveyor {
    public final ConcurrentHashMap<Integer, User> clients;
    public final ConcurrentHashMap<Integer, com.esotericsoftware.kryonet.Connection> connections;
    public final Session session;
    public final Vector<Request> requests;
    public final Vector<Request> responses;
    public Boolean isWorking;

    public Conveyor() {
        this.clients = new ConcurrentHashMap<>();
        this.connections = new ConcurrentHashMap<>();
        this.requests = new Vector<>();
        this.responses = new Vector<>();
        this.session = new Session("", -1);
        this.isWorking = true;
    }
}
