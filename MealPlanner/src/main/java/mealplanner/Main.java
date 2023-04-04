package mealplanner;

import java.sql.*;

public class Main {

    public static Meals meals = new Meals();
    public static Database db = null;

    public static void main(String[] args) {
        try {
            db = new Database();
        } catch (SQLException e) {
            System.out.println(e);
            return;
        }
        UserInterface UI = new UserInterface();
        UI.run();
    }

}
