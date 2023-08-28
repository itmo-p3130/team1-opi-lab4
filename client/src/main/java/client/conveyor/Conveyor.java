package client.conveyor;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryonet.Client;

public class Conveyor {
    public final ConcurrentHashMap<UUID, User> clients;
    public final Vector<Request> requests;
    public final Vector<Request> responses;
    public final Client client;
    public String userID;
    public boolean started;

    public Conveyor() {
        this.requests = new Vector<>();
        this.responses = new Vector<>();
        this.clients = new ConcurrentHashMap<>();
        this.client = new Client();
        this.userID = UUID.randomUUID().toString();
        this.started = false;
    }
}
