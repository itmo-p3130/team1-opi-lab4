package client.conveyor;

import java.util.HashMap;

public class Commander extends Thread {
    private Conveyor conveyor;

    public Commander(Conveyor conveyor) {
        this.conveyor = conveyor;
    }

    @Override
    public void run() {
        while (conveyor.client.isConnected()) {
            if (conveyor.requests.isEmpty()) {
                synchronized (conveyor.requests) {
                    try {
                        conveyor.requests.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                command();
            }
        }
    }

    public void command() {
        while (!conveyor.requests.isEmpty()) {
            Request req = conveyor.requests.firstElement();
            System.err.println((String) req.getType());
            RequestConstants rq = RequestConstants.valueOf((String) req.getType());
            switch (rq) {
                case SET_GAME_START -> {
                    conveyor.started = true;
                    HashMap<Object, Object> mapData = req.getData();
                }
                case GET_DATA_FROM_GAME_SESSION -> {
                    HashMap<Object, Object> mapData = req.getData();
                }
                case UUID_REGISTRATION -> {
                    conveyor.userID = req.getInitialization();
                }
            }
            conveyor.requests.remove(req);
        }
    }
}
