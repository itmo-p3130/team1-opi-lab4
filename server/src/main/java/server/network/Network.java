package server.network;

import java.io.IOException;
import java.io.ObjectInputFilter.Config;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import server.confirmer.Confirmer;
import server.conveyor.Conveyor;
import server.conveyor.Request;
import server.user.User;

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

            public void connected(Connection con) {
                UUID uid = Confirmer.getToken();
                conveyor.clients.put(uid, new User(con.getRemoteAddressTCP().toString(), uid, con));
                System.out.println(conveyor.clients);
            }

        });
    }
}
