package server;

import server.commander.Commander;
import server.confirmer.Confirmer;
import server.conveyor.Conveyor;
import server.conveyor.cards.CardsTower;
import server.network.Network;
import server.network.ResponsesCommander;
import server.session.SessionCommander;
import server.session.Session;

public class Server {
    static public void main(String[] args) {
        Conveyor conveyor = new Conveyor();
        Network network = new Network(conveyor);
        Commander commander = new Commander(conveyor);
        SessionCommander sessionCommander = new SessionCommander(conveyor);
        ResponsesCommander responsesCommander = new ResponsesCommander(conveyor);
        network.start();
        commander.start();
        sessionCommander.start();
        responsesCommander.start();
        try {
            network.join();
            commander.join();
            sessionCommander.join();
            responsesCommander.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
