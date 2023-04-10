package carsharing.logic;

import carsharing.Companies;
import carsharing.Company;
import static carsharing.CarSharing.companies;

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

    public Connection establishConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL);
        connection.setAutoCommit(true);
        return connection;
    }

    public void closeConnection() throws SQLException {
        this.conn.close();
    }

    public void establishDatabase() throws SQLException {
        // Register JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Execute a query that initializes a table "company"
        Statement stmt = conn.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS company ("
                + "id INTEGER AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(30) NOT NULL UNIQUE"
                + ")";
        stmt.executeUpdate(query);

        // Clean-up environment
        stmt.close();
    }

    public void executeQuery(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }
    
    public void loadDataToProgram() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM company");
        
        while(rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            
            Company company = new Company(id, name);
            // loading directly through List interface
            // because add command of Companies
            // executes DB queries as well
            // and that we do not want while initializing db
            companies.getCompanies().add(company);
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
    
    public void dropTable(String table) throws SQLException {
        Statement stmt = conn.createStatement();
        String query = String.format("DROP TABLE %s", table);
        stmt.executeUpdate(query);
        System.out.println("Successfully dropped table " + table);
        stmt.close();
    }
}
