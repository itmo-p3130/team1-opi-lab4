package server;

import server.confirmer.Confirmer;
import server.conveyor.Conveyor;
import server.network.Network;

public class Server {
    static public void main(String[] args) {
        Network n = new Network(new Conveyor());
        n.run();
    }
}
