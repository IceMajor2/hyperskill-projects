package cinema;

import java.util.UUID;

public class Ticket {

    private UUID token;
    private Seat seat;

    public Ticket() {

    }

    public Ticket(UUID token, Seat seat) {
        this.token = token;
        this.seat = seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Seat getSeat() {
        return seat;
    }

    public UUID getToken() {
        return token;
    }
}
