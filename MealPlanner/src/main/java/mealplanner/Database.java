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
        return connection;
    }
    
    public void close() throws SQLException {
        this.connection.close();
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
                + "ingredient_id DECIMAL,"
                + "meal_id INTEGER"
                + ")");
        statement.close();
    }

    public void addMealToDb(Meal meal) throws SQLException {
        String category = meal.getCategory();
        String name = meal.getName();
        int id = meal.getMealId() / 1000;

        Statement statement = connection.createStatement();
        statement.executeUpdate(String.format("INSERT INTO meals (category, meal, meal_id) "
                + "VALUES ('%s', '%s', %d)", category, name, id));
        statement.close();
    }

    public void addIngredientsToDb(Meal meal) throws SQLException {
        String[] ingredients = meal.getIngredients();
        int mealId = meal.getMealId();

        Statement statement = connection.createStatement();
        for (int i = 0; i < ingredients.length; i++) {
            String ingredient = ingredients[i];
            long ingredientId = ingredient.hashCode();
            
            statement.executeUpdate(String.format("INSERT INTO ingredients (ingredient, ingredient_id, meal_id) "
                    + "VALUES ('%s', %d, %d)", ingredient, ingredientId, mealId));
        }
        statement.close();
    }
}
