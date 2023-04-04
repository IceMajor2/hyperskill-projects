
package mealplanner;

public class Meal {

    public static int mealCount = 0;
    private int mealId;
    private String name;
    private String[] ingredients;
    private String category;
    
    public Meal(String category, String name, String[] ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
        
        mealCount++;
        this.mealId = mealCount;
    }

    public String getName() {
        return name;
    }

    public int getMealId() {
        return mealId;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String getCategory() {
        return category;
    }
    
    public String toString() {
        var toReturn = new StringBuilder(String.format
        ("Category: %s%nName: %s%nIngredients:%n", category, name));
        for(String ingredient : ingredients) {
            toReturn.append(ingredient);
            toReturn.append("\n");
        }
        return toReturn.toString();
    }
    
}
