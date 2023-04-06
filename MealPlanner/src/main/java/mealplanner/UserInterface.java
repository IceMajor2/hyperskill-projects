package mealplanner;

import java.util.Scanner;
import static mealplanner.MealPlanner.*;
import java.sql.SQLException;
import java.util.List;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("What would you like to do (add, show, plan, exit)?");
            String usrCommand = scanner.nextLine();
            if ("exit".equals(usrCommand)) {
                System.out.println("Bye!");
                try {
                    db.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
                break;
            }
            if ("add".equals(usrCommand)) {
                addMeal();
                continue;
            }
            if ("show".equals(usrCommand)) {
                System.out.println("Which category do you want to print "
                        + "(breakfast, lunch, dinner)?");
                String category = scanner.nextLine();
                show(category);
                continue;
            }
            if ("drop".equals(usrCommand)) {
                dropDbTables();
                continue;
            }
            if ("plan".equals(usrCommand)) {
                plan();
                continue;
            }
        }

    }

    private void plan() {
        try {
            db.initPlan();
        } catch (SQLException e) {
            System.out.println(e);
        }
        List<String> weekDays = List.of("Monday", "Tuesday", "Wednesday", "Thursday",
                "Friday", "Saturday", "Sunday");
        List<String> categories = List.of("breakfast", "lunch", "dinner");

        for (String weekDay : weekDays) {
            System.out.println(weekDay);

            for (String category : categories) {
                List<Meal> catMeals = meals.getMeals(category);
                catMeals.sort((c1, c2) -> {
                    return c1.getName().compareTo(c2.getName());
                });
                for(Meal catMeal : catMeals) {
                    System.out.println(catMeal.getName());
                }

                System.out.println(String.format("Choose the %s for %s from the list above:", category, weekDay));

                String nameInput = scanner.nextLine();
                while (!meals.contains(nameInput)) {
                    System.out.println("This meal doesn’t exist. "
                            + "Choose a meal from the list above.");
                    nameInput = scanner.nextLine();
                }
                Meal chosenMeal = meals.get(nameInput);
                try {
                    db.addMealToPlan(chosenMeal, weekDay);
                } catch(SQLException e) {
                    System.out.println(e);
                }
            }
            System.out.println(String.format("Yeah! We planned the meals for %s.", weekDay));
        }
        System.out.println("");
        for (String weekDay : weekDays) {
            System.out.println(weekDay);

            for (String cat : categories) {
                try {
                    Meal planned = db.getPlannedMeal(weekDay, cat);
                    char chAt0 = cat.charAt(0);
                    cat = cat.replace(chAt0, (char) (chAt0 - 32));
                    System.out.print(cat + ": " + planned.getName() + "\n");
                } catch (SQLException e) {
                    System.out.println(e);
                }
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
        String[] ingredients = scanner.nextLine().split(",");
        for(String ingredient : ingredients) {
            ingredient = ingredient.trim();
        }

        while (!areIngredientsValid(ingredients)) {
            ingredients = scanner.nextLine().split(",");
            for(String ingredient : ingredients) {
                ingredient = ingredient.trim();
            }
        }

        Meal meal = new Meal(category, name, ingredients);
        meals.add(meal);
        try {
            db.addMealToDb(meal);
            db.addIngredientsToDb(meal);
        } catch (SQLException e) {
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

    private void show(String category) {
        while (!isCategoryValid(category)) {
            category = scanner.nextLine();
        }
        List<Meal> catMeals = meals.getMeals(category);

        if (catMeals.isEmpty()) {
            System.out.println("No meals found.");
            return;
        }

        System.out.println(String.format("Category: %s", category));
        System.out.println();
        for (Meal meal : catMeals) {
            System.out.println(String.format("Name: %s", meal.getName()));
            System.out.println("Ingredients:");
            for (String ingredient : meal.getIngredients()) {
                System.out.println(ingredient);
            }
            System.out.println("");
        }
    }

    private void dropDbTables() {
        try {
            db.dropTables();
        } catch (SQLException e) {
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
        if (category.isEmpty()) {
            System.out.println("Wrong format. Use letters only!");
            return false;
        }
        return true;
    }

    private boolean isNameValid(String name) {
        if (name.isEmpty()) {
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
            if (ingredient.trim().isEmpty()) {
                System.out.println("Wrong format. Use letters only!");
                return false;
            }
        }
        return true;
    }
}
