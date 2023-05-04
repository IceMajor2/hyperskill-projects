package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Seat {
    private int row;
    private int column;
    private int price;
    @JsonIgnore
    private boolean taken;

    public Seat() {
    }

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.taken = false;

        this.price = row <= 4 ? 10 : 8;
    }

    public void take() {
        if (this.taken) {
            // throw some exception
        }
        taken = true;
    }

    @Override
    public boolean equals(Object compare) {
        if (this == compare) {
            return true;
        }

        if (!(compare instanceof Seat)) {
            return false;
        }

        Seat compareSeat = (Seat) compare;

        return this.row == compareSeat.row
                && this.column == compareSeat.column
                && this.taken == compareSeat.taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public int getColumn() {
        return column;
    }

    public boolean isTaken() {
        return taken;
    }

    public int getRow() {
        return row;
    }

    public int getPrice() {
        return price;
    }
}
