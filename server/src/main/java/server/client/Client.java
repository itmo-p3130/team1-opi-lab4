package server.client;

public class Client {
    private String ip;
    private int init;

    public Client(String ipAdress, int initializationCode) {
        this.ip = ipAdress;
        this.init = initializationCode;
    }

    public String getIp() {
        return this.ip;
    }

    public int getInitialization() {
        return this.init;
    }
}
