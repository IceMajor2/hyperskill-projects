package carsharing;

public class Company {
    
    private static int AUTO_INCREMENT_AT = 1;
    private int id;
    private String name;
    
    public Company(int id, String name) {
        this.id = id;
        this.name = name;
        AUTO_INCREMENT_AT++;
    }
    
    public Company(String name) {
        this.id = AUTO_INCREMENT_AT;
        this.name = name;
        AUTO_INCREMENT_AT++;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
