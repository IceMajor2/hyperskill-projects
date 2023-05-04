package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonPropertyOrder({"total_rows", "total_columns", "available_seats"})
public class CinemaRoom {

    private int rows;
    private int columns;
    private List<Seat> seats;
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

        this.seats = new ArrayList<>(availableSeats);
    }

    public void purchaseSeat(int row, int column) {
        Seat seat = this.getSeat(row, column);

        if(seat.isTaken()) {
            // throw some exception
        }
    }

    private int seatListPosition(int row, int column) {
        return ((row - 1) * this.columns) + column - 1;
    }

    private Seat getSeat(int row, int column) {
        return this.seats.get(seatListPosition(row, column));
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
