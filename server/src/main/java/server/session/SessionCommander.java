package server.session;

import server.conveyor.Conveyor;
import server.network.Request;

public class SessionCommander extends Thread {
    private Conveyor conveyor;

    public SessionCommander(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void run() {
        while (conveyor.isWorking) {
            if (conveyor.sessions.isEmpty()) {
                synchronized (conveyor.sessions) {
                    try {
                        conveyor.sessions.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                dodgeSessions();
            }
        }
    }

    private void dodgeSessions() {
        conveyor.sessions.forEach(this::handleSession);
    }

    private void handleSession(String name, Session session) {

    }

    private void handleRequest(Request req) {

    }
}