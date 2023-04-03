
package mealplanner;

public class Main {

    public static Meals meals = new Meals();
    
    public static void main(String[] args) {
        UserInterface UI = new UserInterface();
        UI.run();
    }
}
