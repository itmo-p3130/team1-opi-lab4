package server.network;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

public class Request {
    private final UUID initialization;
    private final HashMap<String, Object> mapData;
    private String requestType;
    private Connection connection;

    public Request(UUID init) {
        this.initialization = init;
        this.mapData = new HashMap<>();
        this.requestType = "";
    }

    public UUID getInitialization() {
        return this.initialization;
    }

    public void addData(String name, Object data) {
        mapData.put(name, data);
    }

    public void setData(String name, Object data) {
        mapData.put(name, data);
    }

    public Map<String, Object> getData() {
        return mapData;
    }

    public Object getData(String name) {
        return mapData.get(name);
    }

    public Set<String> getKeys() {
        return mapData.keySet();
    }

    public void setType(String type) {
        this.requestType = type;
    }

    public String getType() {
        return this.requestType;
    }

    public void setConnection(Connection con) {
        this.connection = con;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
