package cinema;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/seats")
    public Seat buySeat(@RequestParam int row, @RequestParam int column) {
        try {
            Seat bought = cinema.purchaseSeat(row, column);
            return bought;
        } catch(IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The number of a row or a column is out of bounds!");
        } catch(IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The ticket has been already purchased!");
        }
    }
}
