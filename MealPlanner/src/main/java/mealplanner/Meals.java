package mealplanner;

import java.util.List;
import java.util.ArrayList;

public class Meals {
    
    private List<Meal> meals;
    
    public Meals() {
        this.meals = new ArrayList<>();
    }
    
    public void add(Meal meal) {
        this.meals.add(meal);
    }
    
    public int size() {
        return this.meals.size();
    }
    
    public void printMeals() {
        for(Meal meal : meals) {
            System.out.println(meal);
        }
    }
    
}
