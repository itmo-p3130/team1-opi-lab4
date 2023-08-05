package server.user;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import server.network.Request;

public class User {
    private String ip;
    private UUID init;
    private Connection connection;

    public User(UUID initializationCode, Connection connection) {
        this.ip = connection.getRemoteAddressTCP().toString();
        this.init = initializationCode;
        this.connection = connection;
    }

    public String getIp() {
        return this.ip;
    }

    public UUID getInitialization() {
        return this.init;
    }

    public void sendRequest(Request req) {
        connection.sendTCP(req);
    }
}
