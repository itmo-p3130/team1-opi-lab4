package server.conveyor;

public class Request {
    private final Integer initialization;
    private final String data;

    public Request(int init, String content) {
        this.initialization = init;
        this.data = content;
    }

    public String getData() {
        return this.data;
    }

    public Integer getInitialization() {
        return this.initialization;
    }
}
