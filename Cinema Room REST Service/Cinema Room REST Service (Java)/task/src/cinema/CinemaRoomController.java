package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaRoomController {

    private CinemaRoom cinema;
    private Map<UUID, Ticket> tickets;

    public CinemaRoomController() {
        this.cinema = new CinemaRoom(9 ,9);
        this.tickets = new HashMap<>();
    }

    @GetMapping("/seats")
    public CinemaRoom getAvailableSeats() {
        return cinema;
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnTicket(UUID token) {
        return null;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> buySeat(@RequestBody Seat seat) {
        int row = seat.getRow();
        int column = seat.getColumn();

        try {

            Seat bought = cinema.purchaseSeat(row, column);
            UUID token = UUID.randomUUID();
            saveTicket(token, bought);

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

    private void saveTicket(UUID token, Seat seat) {
        Ticket newTicket = new Ticket(token, seat);
        tickets.put(token, newTicket);
    }
}
