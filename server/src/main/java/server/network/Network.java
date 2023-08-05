package server.network;

import java.io.IOException;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import server.conveyor.Conveyor;

public class Network extends Thread {
    private final Conveyor conveyor;
    private final Server server;

    public Network(Conveyor conveyor) {
        this.conveyor = conveyor;
        this.server = new Server();
    }

    @Override
    public void run() {
        this.addListeners();
        try {
            this.server.bind(15000);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.server.start();
    }

    private void addListeners() {
        this.server.addListener(new ConveyorListener(this.conveyor) {
            @Override
            public void connected(Connection con) {
                conveyor.connections.put(con, UUID.randomUUID());
            }

            @Override
            public void received(Connection con, Object obj) {
                if (obj instanceof Request) {
                    Request req = (Request) obj;
                    conveyor.requests.add(req);
                }
            }

            @Override
            public void disconnected(Connection con) {
                conveyor.connections.remove(con);
            }

            @Override
            public void idle(Connection con) {

            }

        });
    }
}
