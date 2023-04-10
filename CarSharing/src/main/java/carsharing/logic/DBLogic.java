package carsharing.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBLogic {

    // JDBC driver name and database URL
    private final String JDBC_DRIVER = "org.h2.Driver";
    private final String DB_URL
            = //"jdbc:h2:./src/carsharing/db/carsharing";
            "jdbc:h2:./src/main/java/carsharing/db/carsharing";
    private Connection conn;

    public DBLogic() throws SQLException {
        this.establishConnection();
        this.establishDatabase();
    }

    public void establishConnection() throws SQLException {
        Connection conn = null;
        conn = DriverManager.getConnection(DB_URL);
        conn.setAutoCommit(true);
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

        // Open a connection
        conn = DriverManager.getConnection(DB_URL);
        conn.setAutoCommit(true);

        // Execute a query that initializes a table "company"
        Statement stmt = conn.createStatement();
        String query = "CREATE TABLE IF NOT EXISTS company ("
                + "id INTEGER,"
                + "name VARCHAR(30)"
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
}
