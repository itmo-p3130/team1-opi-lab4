package server.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import server.commander.RequestConstants;
import server.conveyor.Conveyor;
import server.conveyor.cards.Card;
import server.conveyor.cards.CardNum;
import server.conveyor.cards.CardSuit;
import server.user.User;

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
        this.server.getKryo().register(RequestConstants.class);
        this.server.getKryo().register(Card.class);
        this.server.getKryo().register(CardNum.class);
        this.server.getKryo().register(CardSuit.class);
        this.server.getKryo().register(ArrayList.class);
        this.server.getKryo().register(Vector.class);
        this.server.getKryo().register(User.class);
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
                Integer uid = con.getID();
                conveyor.connections.put(uid, con);
                System.err.println("New client: " + uid);
                System.err.println("Now there is: " + conveyor.connections.size());
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
                conveyor.connections.remove(con.getID());
                conveyor.clients.remove(con.getID());
                conveyor.session.removePlayer(con.getID());
            }

            @Override
            public void idle(com.esotericsoftware.kryonet.Connection con) {

            }

        });
    }
}
