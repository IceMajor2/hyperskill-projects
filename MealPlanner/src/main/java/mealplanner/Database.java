package mealplanner;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import static mealplanner.Main.meals;

public class Database {

    private Connection connection;

    public Database() throws SQLException {
        this.connection = this.establishConnection();
        this.initializeTables();
        this.loadDb();
    }

    /*            meals
        |  CATEGORY  |     MEAL      | MEAL_ID |
    
                  ingredients
        | INGREDIENT | INGREDIENT_ID | MEAL_ID |
     */
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
                + "ingredient_id INTEGER,"
                + "meal_id INTEGER"
                + ")");
        statement.close();
    }

    private void loadDb() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM meals");
        // loading data from meals table
        while (rs.next()) {
            String category = rs.getString("category");
            String name = rs.getString("meal");

            Meal meal = new Meal(category, name);
            meals.add(meal);
        }
        // loading data from ingredients table
        rs = statement.executeQuery("SELECT * FROM ingredients");

        List<String> ingredients = new ArrayList<>();
        int previousMealId = -1;
        while (rs.next()) {
            String ingredient = rs.getString("ingredient");
            int mealId = rs.getInt("meal_id");

            if ((previousMealId != -1 && previousMealId != mealId) || rs.isLast()) {
                if (rs.isLast()) {
                    ingredients.add(ingredient);
                    String[] ingredientsArr = new String[ingredients.size()];
                    ingredients.toArray(ingredientsArr);
                    meals.getId(previousMealId).setIngredients(ingredientsArr);
                    break;
                }
                String[] ingredientsArr = new String[ingredients.size()];
                ingredients.toArray(ingredientsArr);
                meals.getId(previousMealId).setIngredients(ingredientsArr);

                ingredients = new ArrayList<>();
            }

            ingredients.add(ingredient);
            previousMealId = mealId;
        }
        statement.close();
        rs.close();
    }

    public void dropTables() throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE meals");
        statement.executeUpdate("DROP TABLE ingredients");
    }

    public void addMealToDb(Meal meal) throws SQLException {
        String category = meal.getCategory();
        String name = meal.getName();
        int id = meal.getMealId();

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
            long ingredientId = ingredient.hashCode() / 100;

            statement.executeUpdate(String.format("INSERT INTO ingredients (ingredient, ingredient_id, meal_id) "
                    + "VALUES ('%s', %d, %d)", ingredient, ingredientId, mealId));
        }
        statement.close();
    }
}
