package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping(path = "/purchase", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buySeat(@RequestParam int row, @RequestParam int column) {
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
