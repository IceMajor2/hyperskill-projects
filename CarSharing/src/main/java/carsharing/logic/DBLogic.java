package carsharing.logic;

import carsharing.entities.Company;
import carsharing.entities.Car;
import static carsharing.CarSharing.companies;
import static carsharing.CarSharing.cars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBLogic {

    // JDBC driver name and database URL
    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL
            = //"jdbc:h2:./src/carsharing/db/carsharing";
            "jdbc:h2:./src/main/java/carsharing/db/carsharing";
    private Connection conn;

    public DBLogic() throws SQLException {
        conn = this.establishConnection();
        this.establishDatabase();
        this.loadDataToProgram();
    }

    private Connection establishConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL);
        connection.setAutoCommit(true);
        return connection;
    }

    public void closeConnection() throws SQLException {
        this.conn.close();
    }

    private void establishDatabase() throws SQLException {
        // Register JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.createCompanyTable();
        this.createCarTable();
        this.createCustomerTable();
    }
    
    private void createCompanyTable() throws SQLException {
        Statement stmt = conn.createStatement();
        String table = "CREATE TABLE IF NOT EXISTS company ("
                + "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(30) NOT NULL UNIQUE"
                + ")";
        stmt.executeUpdate(table);
        stmt.close();
    }
    
    private void createCarTable() throws SQLException {
        Statement stmt = conn.createStatement();
        String table = "CREATE TABLE IF NOT EXISTS car ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(25) NOT NULL UNIQUE,"
                + "company_id INT NOT NULL,"
                + "CONSTRAINT car_fk FOREIGN KEY (company_id) "
                + "REFERENCES company (id))";
        stmt.executeUpdate(table);
        stmt.close();
    }
    
    private void createCustomerTable() throws SQLException {
        Statement stmt = conn.createStatement();
        String table = "CREATE TABLE IF NOT EXISTS customer ("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(30) NOT NULL UNIQUE,"
                + "rented_car_id INT,"
                + "CONSTRAINT customer_fk FOREIGN KEY (rented_car_id) "
                + "REFERENCES car (id))";
        stmt.executeUpdate(table);
        stmt.close();
    }

    public void executeQuery(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

    private void loadDataToProgram() throws SQLException {
        this.loadCompanies();
        this.loadCars();
    }

    private void loadCompanies() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM company "
                + "ORDER BY id");
        
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");

            Company company = new Company(id, name);
            companies.getCompanies().add(company);
        }

        stmt.close();
        rs.close();
    }

    private void loadCars() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM car "
                + "ORDER BY id");

        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int companyId = rs.getInt("company_id");

            Car car = new Car(id, name, companyId);
            cars.getCars().add(car);
        }

        stmt.close();
        rs.close();
    }

    public void addCompany(Company company) throws SQLException {
        int id = company.getId();
        String name = company.getName();

        Statement stmt = conn.createStatement();
        String query = String.format("INSERT INTO company (id, name) "
                + "VALUES (%d, '%s')", id, name);
        stmt.executeUpdate(query);
        stmt.close();
    }
    
    public void addCar(Car car) throws SQLException {
        int id = car.getId();
        String name = car.getName();
        int companyId = car.getCompanyId();
        
        Statement stmt = conn.createStatement();
        String query = String.format("INSERT INTO car (id, name, company_id) "
                + "VALUES (%d, '%s', %d)", id, name, companyId);
        stmt.executeUpdate(query);
        stmt.close();
    }

    public void dropTable(String table) throws SQLException {
        Statement stmt = conn.createStatement();
        String query = String.format("DROP TABLE %s", table);
        stmt.executeUpdate(query);
        System.out.println("Successfully dropped table " + table);
        stmt.close();
    }
}
