package server.confirmer;

import java.util.UUID;

public class Confirmer {
    private Confirmer() {

    }

    public static UUID getToken() {
        return UUID.randomUUID();
    }
}
