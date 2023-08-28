package client.conveyor;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

public class User {
    private String init;
    Integer cards;

    public User(String initializationCode) {
        this.init = initializationCode;
        cards = 0;
    }

    public String getInitialization() {
        return this.init;
    }

    public Integer getCardsNumber() {
        return this.cards;
    }

    public void setCardsNumber(Integer num) {
        this.cards = num;
    }
}
