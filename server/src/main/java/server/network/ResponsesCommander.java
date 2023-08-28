package server.network;

import server.conveyor.Conveyor;

public class ResponsesCommander extends Thread {
    private Conveyor conveyor;

    public ResponsesCommander(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void run() {
        while (conveyor.isWorking) {
            if (conveyor.responses.isEmpty()) {
                synchronized (conveyor.responses) {
                    try {
                        conveyor.responses.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                sendBack();
            }
        }
    }

    private void sendBack() {
        while (!conveyor.responses.isEmpty()) {
            Request response = conveyor.responses.get(0);
            conveyor.connections.get(response.getInitialization()).sendTCP(response);
            System.err.println("Sended:" + response.getInitialization() + ":" + response.getType());
            conveyor.responses.remove(response);
        }
    }

}