package advisor.repositories;

import advisor.models.Category;

import java.util.*;

public class CategoriesRepository {

    private Map<String, Category> categories;

    public CategoriesRepository() {
        this.categories = new LinkedHashMap<>();
    }

    public int size() {
        return categories.size();
    }

    public Category get(String name) {
        return this.categories.get(name);
    }

    public void put(Collection<Category> categories) {
        for (Category category : categories) {
            this.categories.put(category.getName(), category);
        }
    }

    public List<Category> asList() {
        return categories.values().stream().toList();
    }
}
