package mealplanner;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import static mealplanner.Main.meals;
import java.util.Map;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;

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

    public Meal getPlannedMeal(String day, String category) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT meal_id "
                + "FROM plan "
                + "WHERE category = '" + category + "' AND "
                + "day = '" + day + "'");
        Meal meal = null;
        while (rs.next()) {
            int mealId = rs.getInt("meal_id");
            meal = meals.getId(mealId);
        }
        return meal;
    }

    public void dropTables() throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE meals");
        statement.executeUpdate("DROP TABLE ingredients");
        statement.executeUpdate("DROP TABLE plan");

        statement.close();
    }

    public void initPlan() throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS plan");
        statement.executeUpdate("CREATE TABLE plan ("
                + "meal_option VARCHAR(30),"
                + "category VARCHAR(20),"
                + "meal_id INTEGER,"
                + "day VARCHAR(10)"
                + ")");
        statement.close();
    }

    public void addMealToPlan(Meal meal, String day) throws SQLException {
        String mealOption = meal.getName();
        String category = meal.getCategory();
        int mealId = meal.getMealId();

        Statement statement = connection.createStatement();
        statement.executeUpdate(String.format("INSERT INTO plan (meal_option, category, meal_id, day) "
                + "VALUES ('%s', '%s', %d, '%s')", mealOption, category, mealId, day));
        statement.close();
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

    public boolean planExists() {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT count(*) FROM plan");

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            if (count == 0) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void saveShoppingList(String file) throws SQLException {
        Map<String, Integer> ingredients = new HashMap<>();
        List<String> weekDays = List.of("Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday", "Sunday");
        List<String> categories = List.of("breakfast", "lunch", "dinner");
        List<Meal> planned = new ArrayList<>();
                
        for(String day : weekDays) {
            for(String category : categories) {
                Meal mealPlanned = this.getPlannedMeal(day, category);
                planned.add(mealPlanned);
            }
        }

        for (Meal meal : planned) {
            for (String ingredient : meal.getIngredients()) {
                ingredient = ingredient.trim();
                if (ingredients.containsKey(ingredient)) {
                    ingredients.put(ingredient, ingredients.get(ingredient) + 1);
                    continue;
                }
                ingredients.put(ingredient, 1);
            }
        }

        try (FileWriter writer = new FileWriter(file)) {
            ingredients.entrySet().stream().forEach((entry) -> {
                try {
                    writer.append(entry.getKey());
                    if (entry.getValue() != 1) {
                        writer.append(" x" + entry.getValue());
                    }
                    writer.append("\n");
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
