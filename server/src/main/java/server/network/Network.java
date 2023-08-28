package server.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import server.conveyor.Conveyor;
import server.conveyor.cards.Card;

public class Network extends Thread {
    private final Conveyor conveyor;
    private final Server server;

    public Network(Conveyor conveyor) {
        this.conveyor = conveyor;
        this.server = new Server();
        this.server.getKryo().register(Request.class);
        this.server.getKryo().register(Object.class);
        this.server.getKryo().register(String.class);
        this.server.getKryo().register(Card.class);
        this.server.getKryo().register(HashMap.class);
    }

    @Override
    public void run() {
        this.addListeners();
        try {
            this.server.bind(15000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.server.start();
    }

    private void addListeners() {
        this.server.addListener(new ConveyorListener(this.conveyor) {
            @Override
            public void connected(com.esotericsoftware.kryonet.Connection con) {
                String uid = UUID.randomUUID().toString();
                conveyor.connections.put(con, uid);
                Request req = new Request(uid);
                con.sendTCP(req);
                System.err.println("New client: " + req.getInitialization());
            }

            @Override
            public void received(Connection con, Object obj) {
                if (obj instanceof Request) {
                    Request req = (Request) obj;
                    req.setConnection(con);
                    System.err.println(req.getType());
                    conveyor.requests.add(req);
                    synchronized (conveyor.requests) {
                        conveyor.requests.notifyAll();
                    }
                }
            }

            @Override
            public void disconnected(com.esotericsoftware.kryonet.Connection con) {
                conveyor.connections.remove(con);
            }

            @Override
            public void idle(com.esotericsoftware.kryonet.Connection con) {

            }

        });
    }
}
