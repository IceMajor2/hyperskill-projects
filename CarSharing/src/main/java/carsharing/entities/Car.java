package carsharing.entities;

public class Car {
    
    private static int AUTO_INCREMENT_AT = 1;
    private int id;
    private String name;
    private int companyId;
    
    public Car(int id, String name, int companyId) {
        this.id = id;
        this.name = name;
        this.companyId = companyId;
        AUTO_INCREMENT_AT++;
    }
    
    public Car(String name, int companyId) {
        this.id = AUTO_INCREMENT_AT;
        this.name = name;
        this.companyId = companyId;
        AUTO_INCREMENT_AT++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }
}
