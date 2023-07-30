package server.conveyor;

import java.util.UUID;

public class Request {
    private final UUID initialization;
    private final String data;

    public Request(UUID init, String content) {
        this.initialization = init;
        this.data = content;
    }

    public String getData() {
        return this.data;
    }

    public UUID getInitialization() {
        return this.initialization;
    }
}
