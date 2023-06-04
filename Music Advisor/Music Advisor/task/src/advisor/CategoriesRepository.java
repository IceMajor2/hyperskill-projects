package advisor;

import advisor.models.Category;

import java.util.HashMap;
import java.util.Map;

public class CategoriesRepository {

    private Map<String, Category> categories;

    public CategoriesRepository() {
        this.categories = new HashMap<>();
    }

    public void put(Category category) {
        this.categories.put(category.getId(), category);
    }

    public Category get(String id) {
        return this.categories.get(id);
    }
}
