package carsharing.entities;

import static carsharing.CarSharing.cars;

public class Customer {
    
    private static int AUTO_INCREMENT_AT = 1;
    private int id;
    private String name;
    private int rentedCarId;
    
    public Customer(int id, String name, int rentedCarId) {
        this.id = id;
        this.name = name;
        this.rentedCarId = rentedCarId;
    }
    
    public Customer(String name, int rentedCarId) {
        this.id = AUTO_INCREMENT_AT;
        this.name = name;
        this.rentedCarId = rentedCarId;
        AUTO_INCREMENT_AT++;
    }
    
    public Customer(String name) {
        this.id = AUTO_INCREMENT_AT;
        this.name = name;
        this.rentedCarId = -1;
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

    public void setRentedCarId(int rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
