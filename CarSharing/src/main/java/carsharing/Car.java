package carsharing;

public class Car {
    
    private static int AUTO_INCREMENT_AT = 1;
    private int id;
    private String name;
    private int companyId;
    
    public Car(int id, String name, Company company) {
        this.id = id;
        this.name = name;
        this.companyId = company.getId();
        AUTO_INCREMENT_AT++;
    }
    
    public Car(String name, Company company) {
        this.id = AUTO_INCREMENT_AT;
        this.name = name;
        this.companyId = company.getId();
        AUTO_INCREMENT_AT++;
    }
}
