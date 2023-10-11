package cinema;

import java.util.UUID;

public class Ticket {

    private Token token;
    private Seat seat;

    public Ticket() {

    }

    public Ticket(Token token, Seat seat) {
        this.token = token;
        this.seat = seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Seat getSeat() {
        return seat;
    }

    public Token getToken() {
        return token;
    }
}
