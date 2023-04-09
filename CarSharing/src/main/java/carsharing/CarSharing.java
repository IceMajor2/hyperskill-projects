package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CarSharing {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL =
            //"jdbc:h2:./src/carsharing/db/carsharing";
            "jdbc:h2:./src/main/java/carsharing/db/carsharing";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);

            // Execute a query
            stmt = conn.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS company ("
                    + "id INTEGER,"
                    + "name VARCHAR(30)"
                    + ")";
            stmt.executeUpdate(query);

            // Clean-up environment
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
