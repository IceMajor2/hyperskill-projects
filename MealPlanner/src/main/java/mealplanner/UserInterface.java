package mealplanner;

import java.util.Scanner;
import static mealplanner.Main.*;
import java.sql.SQLException;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("What would you like to do (add, show, exit)?");
            String usrCommand = scanner.nextLine();
            if ("exit".equals(usrCommand)) {
                System.out.println("Bye!");
                try {
                    db.close();
                } catch(SQLException e) {
                    System.out.println(e);
                }
                break;
            }
            if ("add".equals(usrCommand)) {
                addMeal();
                continue;
            }
            if ("show".equals(usrCommand)) {
                show();
                continue;
            }
            if("drop".equals(usrCommand)) {
                dropDbTables();
                continue;
            }
        }

    }

    private void addMeal() {
        System.out.println("Which meal do you want to add "
                + "(breakfast, lunch, dinner)?");
        String category = scanner.nextLine();

        while (!isCategoryValid(category)) {
            category = scanner.nextLine();
        }

        System.out.println("Input the meal's name:");
        String name = scanner.nextLine();

        while (!isNameValid(name)) {
            name = scanner.nextLine();
        }

        System.out.println("Input the ingredients:");
        String[] ingredients = scanner.nextLine().split(", ");

        while (!areIngredientsValid(ingredients)) {
            ingredients = scanner.nextLine().split(",");
        }

        Meal meal = new Meal(category, name, ingredients);
        meals.add(meal);
        try {
            db.addMealToDb(meal);
        db.addIngredientsToDb(meal);
        } catch(SQLException e) {
            System.out.println(e);
        }
        
        System.out.println("The meal has been added!");
    }

    private void show() {
        if (meals.size() == 0) {
            System.out.println("No meals saved. Add a meal first.");
            return;
        }
        meals.printMeals();
    }
    
    private void dropDbTables() {
        try {
            db.dropTables();
        } catch(SQLException e) {
            System.out.println(e);
        }
    }

    private boolean isRegexValid(String input) {
        String regex = "[A-Za-z -]+";
        if (!input.matches(regex)) {
            System.out.println("Wrong format. Use letters only!");
            return false;
        }
        return true;
    }

    private boolean isCategoryValid(String category) {
        if (!(category.equals("breakfast") || category.equals("lunch")
                || category.equals("dinner"))) {
            System.out.println("Wrong meal category! "
                    + "Choose from: breakfast, lunch, dinner.");
            return false;
        }
        if (!isRegexValid(category)) {
            return false;
        }
        if(category.isEmpty()) {
            System.out.println("Wrong format. Use letters only!");
            return false;
        }
        return true;
    }

    private boolean isNameValid(String name) {
        if(name.isEmpty()) {
            System.out.println("Wrong format. Use letters only!");
            return false;
        }
        return isRegexValid(name);
    }

    private boolean areIngredientsValid(String[] ingredients) {
        for (String ingredient : ingredients) {
            if (!isRegexValid(ingredient)) {
                return false;
            }
            if(ingredient.trim().isEmpty()) {
                System.out.println("Wrong format. Use letters only!");
                return false;
            }
        }
        return true;
    }
}