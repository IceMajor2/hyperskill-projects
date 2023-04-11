package carsharing.entities;

public class Customer {
    
    private static int AUTO_INCREMENT_AT = 1;
    private int id;
    private String name;
    private int rentedCarId;
    
    public Customer(String name, int rentedCarId) {
        this.id = AUTO_INCREMENT_AT;
        this.name = name;
        this.rentedCarId = rentedCarId;
        AUTO_INCREMENT_AT++;
    }

    public int getRentedCarId() {
        return rentedCarId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
