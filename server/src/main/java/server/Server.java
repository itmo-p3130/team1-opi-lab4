package server;

import server.commander.Commander;
import server.confirmer.Confirmer;
import server.conveyor.Conveyor;
import server.network.Network;
import server.session.SessionCommander;
import server.session.Session;

public class Server {
    static public void main(String[] args) {
        Conveyor conveyor = new Conveyor();
        Network network = new Network(conveyor);
        Commander commander = new Commander(conveyor);
        SessionCommander sessionCommander = new SessionCommander(conveyor);
        network.start();
        commander.start();
        sessionCommander.start();
        try {
            network.join();
            commander.join();
            sessionCommander.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
