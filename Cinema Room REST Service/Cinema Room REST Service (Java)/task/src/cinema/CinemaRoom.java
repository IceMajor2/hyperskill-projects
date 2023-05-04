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

    public Seat purchaseSeat(int row, int column) {
        Seat seat = null;
        try {
            seat = this.getSeat(row, column);
        } catch(IndexOutOfBoundsException e) {
            throw e;
        }

        seat.take();
        this.removeSeatFromAvailable(seat);
        return seat;
    }

    private void removeSeatFromAvailable(Seat seat) {
        for(Seat avSeat : availableSeats) {
            if(avSeat.equals(seat)) {
                availableSeats.remove(avSeat);
                return;
            }
        }
        throw new RuntimeException();
    }

    private int seatListPosition(int row, int column) throws IndexOutOfBoundsException {
        if(row <= 0 || column <= 0 || row > rows || column > columns) {
            throw new IndexOutOfBoundsException();
        }
        return ((row - 1) * this.columns) + column - 1;
    }

    private Seat getSeat(int row, int column) throws IndexOutOfBoundsException {
        try {
            int seatPos = seatListPosition(row, column);
            return this.seats.get(seatPos);
        } catch(IndexOutOfBoundsException e) {
            throw e;
        }
    }

    private int totalSeats() {
        return seats.size();
    }

    private int availableSeatsNumber() { return availableSeats.size(); }

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
