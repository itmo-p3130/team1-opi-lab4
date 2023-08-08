package server.network;

import com.esotericsoftware.kryonet.Listener;

import server.conveyor.Conveyor;

import com.esotericsoftware.kryonet.Connection;

public class ConveyorListener extends Listener {
    private Conveyor conveyor;

    public ConveyorListener(Conveyor conv) {
        this.conveyor = conv;
    }

    public void connected(Connection connection) {
    }

    public void disconnected(Connection connection) {
    }

    public void received(Connection connection, Object object) {
    }

    public void idle(Connection connection) {
    }

}
