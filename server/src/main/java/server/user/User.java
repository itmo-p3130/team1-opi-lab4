package server.user;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import server.network.Request;
import server.session.Session;

public class User {
    private String ip;
    private String init;
    private Connection connection;
    private Session session;

    public User(String initializationCode, Connection connection) {
        this.ip = connection.getRemoteAddressTCP().toString();
        this.init = initializationCode;
        this.connection = connection;
    }

    public String getIp() {
        return this.ip;
    }

    public String getInitialization() {
        return this.init;
    }

    public void sendRequest(Request req) {
        connection.sendTCP(req);
    }

    public void setSession(Session ses) {
        this.session = ses;
    }

    public Session getSession() {
        return this.session;
    }
}
