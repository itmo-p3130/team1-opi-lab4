package server.conveyor;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.esotericsoftware.kryonet.Client;

public class Conveyor {
    public final ConcurrentHashMap<Integer, Client> clients;
    public final Vector<Request> requests;

    public Conveyor() {
        this.clients = new ConcurrentHashMap<>();

    }
}
