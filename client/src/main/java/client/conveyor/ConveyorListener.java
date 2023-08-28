package client.conveyor;

import com.esotericsoftware.kryonet.Listener;

import client.conveyor.Conveyor;

import com.esotericsoftware.kryonet.Connection;

public class ConveyorListener extends Listener {
    private Conveyor conveyor;

    public ConveyorListener(Conveyor conv) {
        this.conveyor = conv;
    }

    public void connected(com.esotericsoftware.kryonet.Connection connection) {
    }

    public void disconnected(com.esotericsoftware.kryonet.Connection connection) {
    }

    public void received(com.esotericsoftware.kryonet.Connection connection, Object object) {
    }

    public void idle(com.esotericsoftware.kryonet.Connection connection) {
    }

}
