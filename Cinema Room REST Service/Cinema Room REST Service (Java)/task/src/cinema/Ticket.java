package cinema;

import java.util.UUID;

public class Ticket {

    private UUID token;
    private Seat seat;

    public Ticket(UUID token, Seat seat) {
        this.token = token;
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public UUID getToken() {
        return token;
    }
}
