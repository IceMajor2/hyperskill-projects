package cinema;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Seat {
    private int row;
    private int column;
    @JsonIgnore
    private boolean taken;

    public Seat() {}

    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.taken = false;
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
}
