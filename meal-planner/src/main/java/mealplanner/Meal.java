
package mealplanner;

public class Meal {

    public static int mealCount = 0;
    private int mealId;
    private String name;
    private String[] ingredients;
    private String category;
    
    public Meal(String category, String name, String[] ingredients) {
        this(category, name);
        this.ingredients = ingredients;
    }
    
    public Meal(String category, String name) {
        this.category = category;
        this.name = name;
        
        mealCount++;
        this.mealId = mealCount;
    }
    
    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
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
