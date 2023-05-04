package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CinemaRoomController {

    private CinemaRoom cinema;

    public CinemaRoomController() {
        this.cinema = new CinemaRoom(9 ,9);
    }

    @GetMapping("/seats")
    public CinemaRoom getAvailableSeats() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> buySeat(@RequestBody Seat seat) {
        int row = seat.getRow();
        int column = seat.getColumn();
        try {
            Seat bought = cinema.purchaseSeat(row, column);
            return new ResponseEntity<>(bought, HttpStatus.OK);
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
}
