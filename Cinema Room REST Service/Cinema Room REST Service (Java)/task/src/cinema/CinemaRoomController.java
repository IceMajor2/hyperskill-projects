package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CinemaRoomController {

    private CinemaRoom cinema;
    private Map<String, String> adminCredentials;

    public CinemaRoomController() {
        this.cinema = new CinemaRoom(9 ,9);

        this.adminCredentials = new HashMap<>();
        adminCredentials.put("password", "super_secret");
    }

    @GetMapping("/seats")
    public CinemaRoom getAvailableSeats() {
        return cinema;
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(@RequestBody Token token) {
        if(!cinema.ticketExists(token)) {
            return new ResponseEntity<>(Map.of("error",
                    "Wrong token!"), HttpStatus.BAD_REQUEST);
        }

        try {
            Ticket refunded = cinema.returnTicket(token);
            return new ResponseEntity<>(Map.of("returned_ticket",
                    refunded.getSeat()), HttpStatus.OK);
        } catch(Exception e) {
            throw new RuntimeException("Weird stuff.");
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> buySeat(@RequestBody Seat seat) {
        int row = seat.getRow();
        int column = seat.getColumn();

        try {
            Token token = new Token(UUID.randomUUID());
            Seat bought = cinema.purchaseSeat(row, column, token);

            return new ResponseEntity<>(Map.of("ticket", bought,
                    "token", token.toString()), HttpStatus.OK);

        } catch(IndexOutOfBoundsException e) {
            return new ResponseEntity<>(Map.of("error",
                    "The number of a row or a column is out of bounds!"),
                    HttpStatus.BAD_REQUEST);

        } catch(IllegalStateException e) {
            return new ResponseEntity<>(Map.of("error",
                    "The ticket has been already purchased!"),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/stats")
    public ResponseEntity<?> getStatistics(@RequestParam Optional<String> password) {
        if(!password.isPresent() || !adminCredentials.get("password").equals(password.get())) {
            return new ResponseEntity<>(Map.of("error",
                    "The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(this.cinema.getStats(), HttpStatus.OK);
    }
}

class Token {
    UUID token;

    public Token() {
    }

    public Token(UUID token) {
        this.token = token;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Token)) {
            return false;
        }

        Token other = (Token) obj;

        return this.token.equals(other.token);
    }

    @Override
    public int hashCode() {
        return this.token.hashCode();
    }

    @Override
    public String toString() {
        return this.token.toString();
    }
}