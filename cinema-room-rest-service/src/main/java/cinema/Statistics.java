package cinema;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Statistics {

    private CinemaRoom cinema;
    private int currentIncome;
    private int availableSeats;
    private int purchasedTickets;

    public Statistics() {

    }

    public Statistics(CinemaRoom room) {
        this.cinema = room;
        this.currentIncome = 0;
        this.availableSeats = room.availableSeatsNumber();
        this.purchasedTickets = room.ticketsNumber();
    }

    public Statistics(int currentIncome, int availableSeats, int purchasedSeats) {
        this.currentIncome = currentIncome;
        this.availableSeats = availableSeats;
        this.purchasedTickets = purchasedSeats;
    }

    @JsonProperty("current_income")
    public int getCurrentIncome() {
        int sum = 0;
        for(Seat seat : cinema.getOrderedSeats()) {
            sum += seat.getPrice();
        }
        this.currentIncome = sum;
        return currentIncome;
    }

    public void setCurrentIncome(int currentIncome) {
        this.currentIncome = currentIncome;
    }

    @JsonProperty("number_of_available_seats")
    public int getAvailableSeats() {
        return cinema.availableSeatsNumber();
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    @JsonProperty("number_of_purchased_tickets")
    public int getPurchasedTickets() {
        return cinema.ticketsNumber();
    }

    public void setPurchasedTickets(int purchasedTickets) {
        this.purchasedTickets = purchasedTickets;
    }
}
