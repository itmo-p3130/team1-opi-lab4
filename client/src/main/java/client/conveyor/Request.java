package client.conveyor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

public class Request {
    private final Integer initialization;
    private final HashMap<Object, Object> mapData;
    private Object requestType;
    private transient com.esotericsoftware.kryonet.Connection connection;

    public Request() {
        this.initialization = -1;
        this.mapData = new HashMap<>();
        this.requestType = "";
    }

    public Request(Integer init) {
        this.initialization = init;
        this.mapData = new HashMap<>();
        this.requestType = "";
    }

    public Integer getInitialization() {
        return this.initialization;
    }

    public void addData(Object name, Object data) {
        mapData.put(name, data);
    }

    public void setData(Object name, Object data) {
        mapData.put(name, data);
    }

    public HashMap<Object, Object> getData() {
        return mapData;
    }

    public Object getData(Object name) {
        return mapData.get(name);
    }

    public Set<Object> getKeys() {
        return mapData.keySet();
    }

    public void setType(Object type) {
        this.requestType = type;
    }

    public Object getType() {
        return this.requestType;
    }

    public void setConnection(com.esotericsoftware.kryonet.Connection con) {
        this.connection = con;
    }

    public com.esotericsoftware.kryonet.Connection getConnection() {
        return (com.esotericsoftware.kryonet.Connection) this.connection;

    }
}
