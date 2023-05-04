package cinema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public void buySeat(int row, int column) {

    }
}
