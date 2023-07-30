package server.network;

import server.conveyor.Conveyor;

public class Network extends Thread {
    private final Conveyor conveyor;

    public Network(Conveyor conveyor) {
        this.conveyor = conveyor;
    }
}
