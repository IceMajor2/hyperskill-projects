package carsharing;

import java.util.ArrayList;
import java.util.List;

public class Companies {

    private List<Company> companies;

    public Companies() {
        this.companies = new ArrayList<>();
    }

    public List<Company> getCompanies() {
        return this.companies;
    }

    public void add(Company company) {
        this.companies.add(company);
    }
    
    public int size() {
        return this.companies.size();
    }

    public Company get(int index) {
        return this.companies.get(index);
    }
}
