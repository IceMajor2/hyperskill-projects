
package mealplanner;



public class Meal {

    private String name;
    private String[] ingredients;
    private String category;
    
    public Meal(String category, String name, String[] ingredients) {
        this.name = name;
        this.category = category;
        this.ingredients = ingredients;
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
