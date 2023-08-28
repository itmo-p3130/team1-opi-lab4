package server.user;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

import server.network.Request;
import server.session.Session;

public class User {
    private transient String ip;
    private Integer init;
    private transient Connection connection;
    private transient Session session;

    public User(Integer initializationCode, Connection connection) {
        this.ip = connection.getRemoteAddressTCP().toString();
        this.init = initializationCode;
        this.connection = connection;
    }

    public User() {

    }

    public String getIp() {
        return this.ip;
    }

    public Integer getInitialization() {
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
