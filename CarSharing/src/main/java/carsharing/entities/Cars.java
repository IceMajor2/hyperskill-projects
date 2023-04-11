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
    
    public void add(Car car) {
        this.cars.add(car);
    }
    
    public int size() {
        return this.cars.size();
    }
    
    public Car get(int index) {
        return this.cars.get(index);
    }
}
