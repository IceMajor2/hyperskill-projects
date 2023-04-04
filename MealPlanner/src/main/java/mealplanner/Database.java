package mealplanner;

import java.sql.*;

public class Database {

    private Connection connection;

    public Database() throws SQLException {
        this.connection = this.establishConnection();
        this.initializeTables();
    }

    private Connection establishConnection() throws SQLException {
        String DB_URL = "jdbc:postgresql:meals_db";
        String USER = "postgres";
        String PASS = "1111";
        connection = DriverManager.getConnection(DB_URL, USER, PASS);
        connection.setAutoCommit(true);
        System.out.println(e);
        return connection;
    }

    private void initializeTables() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS meals ("
                + "category VARCHAR(20),"
                + "meal VARCHAR(30),"
                + "meal_id INTEGER"
                + ")");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS ingredients ("
                + "ingredient VARCHAR(20),"
                + "ingredient_id INTEGER,"
                + "meal_id INTEGER"
                + ")");

        // ResultSet rs = statement.executeQuery("select * from meals");
        statement.close();
        connection.close();
    }
}
