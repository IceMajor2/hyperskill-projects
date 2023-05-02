package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"total_rows", "total_columns", "available_seats"})
public class CinemaRoom {

    private int rows;
    private int columns;
    private List<Seat> availableSeats;

    public CinemaRoom() {}

    public CinemaRoom(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;
        this.initSeats();
    }

    private void initSeats() {
        this.availableSeats = new ArrayList<>();
        for(int i = 1; i <= rows; i++) {
            for(int y = 1; y <= columns; y++) {
                Seat seat = new Seat(i, y);
                availableSeats.add(seat);
            }
        }
    }

    public int totalSeats() {
        return availableSeats.size();
    }

    @JsonProperty(value = "total_columns")
    public int getTotalColumns() {
        return columns;
    }

    @JsonProperty(value = "total_rows")
    public int getTotalRows() {
        return rows;
    }

    @JsonProperty(value = "available_seats")
    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }
}
