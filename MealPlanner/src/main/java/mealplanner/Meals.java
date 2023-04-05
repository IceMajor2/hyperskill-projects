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
    
    public Meal getId(int id) {
        for(Meal meal : meals) {
            if(meal.getMealId() == id) {
                return meal;
            }
        }
        return null;
    }
    
    public Meal get(int index) {
        return this.get(index);
    }
    
    public List<Meal> getMeals(String category) {
        List<Meal> categoryMeals = new ArrayList<>();
        for(Meal meal : meals) {
            if(meal.getCategory().equals(category)) {
                categoryMeals.add(meal);
            }
        }
        return categoryMeals;
    }
}
