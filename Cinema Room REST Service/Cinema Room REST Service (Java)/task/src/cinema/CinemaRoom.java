package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"total_rows", "total_columns", "available_seats"})
public class CinemaRoom {

    private int rows;
    private int columns;

    private List<Seat> seats;
    private List<Seat> availableSeats;
    private List<Seat> orderedSeats;

    private Map<Token, Ticket> tickets;
    private Statistics stats;

    public CinemaRoom() {
    }

    public CinemaRoom(int rows, int cols) {
        this.rows = rows;
        this.columns = cols;

        this.tickets = new HashMap<>();
        this.initSeats();

        this.stats = new Statistics(this);
    }

    public Seat purchaseSeat(int row, int column, Token token) throws IllegalStateException, IndexOutOfBoundsException {
        Seat seat = null;
        try {
            seat = this.getSeat(row, column);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }

        if (seat.isTaken()) {
            throw new IllegalStateException();
        }

        saveTicket(token, seat);
        seat.take();
        this.availableSeats.remove(seat);
        this.orderedSeats.add(seat);

        return seat;
    }

    public Ticket returnTicket(Token token) throws IndexOutOfBoundsException {
        Ticket ticket = this.tickets.get(token);
        Seat toRefund = ticket.getSeat();

        this.orderedSeats.remove(toRefund);
        this.returnSeat(toRefund.getRow(), toRefund.getColumn());
        tickets.remove(token);
        return ticket;
    }

    private void returnSeat(int row, int column) throws IndexOutOfBoundsException {
        Seat seat = null;
        try {
            seat = this.getSeat(row, column);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }

        if (!seat.isTaken()) {
            throw new IllegalStateException();
        }

        seat.vacate();
        this.addSeatToAvailable(seat);
    }

    private void addSeatToAvailable(Seat seat) {
        int seatPos = this.seatListPosition(seat.getRow(), seat.getColumn());
        this.availableSeats.add(seatPos, seat);
    }

    private boolean seatExists(int row, int column) {
        if (row <= 0 || column <= 0 || row > rows || column > columns) {
            return false;
        }
        return true;
    }

    public boolean ticketExists(Token token) {
        return this.tickets.containsKey(token);
    }

    private int seatListPosition(int row, int column) throws IndexOutOfBoundsException {
        if (!seatExists(row, column)) {
            throw new IndexOutOfBoundsException();
        }
        return ((row - 1) * this.columns) + column - 1;
    }

    private Seat getSeat(int row, int column) throws IndexOutOfBoundsException {
        try {
            int seatPos = seatListPosition(row, column);
            return this.seats.get(seatPos);
        } catch (IndexOutOfBoundsException e) {
            throw e;
        }
    }

    // getters and setters + more

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

    private void initSeats() {
        this.availableSeats = new ArrayList<>();
        this.orderedSeats = new ArrayList<>();
        for (int i = 1; i <= rows; i++) {
            for (int y = 1; y <= columns; y++) {
                Seat seat = new Seat(i, y);
                availableSeats.add(seat);
            }
        }

        this.seats = new ArrayList<>(availableSeats);
    }

    public int availableSeatsNumber() {
        return availableSeats.size();
    }

    public int totalSeats() {
        return seats.size();
    }

    public Ticket getTicket(Token token) {
        return this.tickets.get(token);
    }

    @JsonIgnore
    public List<Seat> getOrderedSeats() {
        return orderedSeats;
    }

    private void saveTicket(Token token, Seat seat) {
        Ticket ticket = new Ticket(token, seat);
        this.tickets.put(token, ticket);
    }

    public int ticketsNumber() {
        return this.tickets.size();
    }

    @JsonIgnore
    public Statistics getStats() {
        return stats;
    }
}
