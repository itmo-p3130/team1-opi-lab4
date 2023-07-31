package server.user;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

public class User {
    private String ip;
    private UUID init;
    private Connection connection;

    public User(String ipAdress, UUID initializationCode, Connection connection) {
        this.ip = ipAdress;
        this.init = initializationCode;
        this.connection = connection;
    }

    public String getIp() {
        return this.ip;
    }

    public UUID getInitialization() {
        return this.init;
    }
}
