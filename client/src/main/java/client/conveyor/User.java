package client.conveyor;

import java.util.UUID;

import com.esotericsoftware.kryonet.Connection;

public class User {
    private Integer init;
    transient Integer cards;

    public User(Integer initializationCode) {
        this.init = initializationCode;
        cards = 0;
    }

    public User() {

    }

    public Integer getInitialization() {
        return this.init;
    }

    public Integer getCardsNumber() {
        return this.cards;
    }

    public void setCardsNumber(Integer num) {
        this.cards = num;
    }
}
