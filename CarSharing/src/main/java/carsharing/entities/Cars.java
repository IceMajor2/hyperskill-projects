package carsharing.entities;

import java.util.ArrayList;
import java.util.List;

public class Cars {
    
    private List<Car> cars;
    
    public Cars() {
        this.cars = new ArrayList<>();
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public List<Car> getCars() {
        return cars;
    }
    
    public List<Car> carsOf(Company company) {
        List<Car> companyCars = new ArrayList<>();
        int companyId = company.getId();
        for(Car car : cars) {
            if(car.getCompanyId() == companyId) {
                companyCars.add(car);
            }
        }
        return companyCars;
    }
    
    public void add(Car car) {
        this.cars.add(car);
    }
    
    public int carCountOf(Company company) {
        int companyId = company.getId();
        int size = 0;
        for(Car car : cars) {
            if(car.getCompanyId() == companyId) {
                size++;
            }
        }
        return size;
    }
    
    public Car get(int index) {
        return this.cars.get(index);
    }
}
