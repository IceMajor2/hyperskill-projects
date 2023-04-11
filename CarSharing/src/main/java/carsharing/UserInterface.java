package carsharing;

import static carsharing.CarSharing.cars;
import static carsharing.CarSharing.companies;
import carsharing.entities.Company;
import carsharing.entities.Car;
import carsharing.logic.ProgramLogic;
import java.util.Scanner;
import java.sql.SQLException;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void startMenu() {
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("0. Exit");
            String choice = this.scanner.nextLine();

            if (choice.equals("0")) {
                break;
            }
            if (choice.equals("drop")) {
                dropTable();
                continue;
            }
            if (choice.equals("1")) {
                managerMenu();
                continue;
            }
        }
    }

    private void managerMenu() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            String choice = this.scanner.nextLine();

            if (choice.equals("0")) {
                break;
            }
            if (choice.equals("1")) {
                if (companies.size() == 0) {
                    System.out.println("The company list is empty!");
                    return;
                }
                System.out.println("Choose the company:");
                printCompanies();
                int id = Integer.valueOf(scanner.nextLine());
                companyMenu(companies.get(id - 1));
                continue;
            }
            if (choice.equals("2")) {
                createCompany();
                continue;
            }
        }
    }

    private void companyMenu(Company company) {
        System.out.println(String.format("'%s' company", company.getName()));
        while (true) {
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                break;
            }
            if (choice.equals("1")) {
                if (cars.carCountOf(company) == 0) {
                    System.out.println("The car list is empty!");
                    return;
                }
                System.out.println("Car list:");
                printCarsOf(company);
                continue;
            }
            if (choice.equals("2")) {
                int companyId = company.getId();
                createCar(companyId);
                continue;
            }
        }
    }

    private void createCompany() {
        System.out.println("Enter the company name:");
        String name = scanner.nextLine();
        Company company = new Company(name);
        try {
            ProgramLogic.completeAdd(company);
            System.out.println("The company was created!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCar(int companyId) {
        System.out.println("Enter the car name:");
        String name = scanner.nextLine();
        Car car = new Car(name, companyId);
        try {
            ProgramLogic.completeAdd(car);
            System.out.println("The car was added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void printCompanies() {
        for (Company company : companies.getCompanies()) {
            int id = company.getId();
            String name = company.getName();
            System.out.println(String.format("%d. %s", id, name));
        }
    }

    private void printCarsOf(Company company) {
        for (Car car : cars.carsOf(company)) {
            int id = car.getId();
            String name = car.getName();
            System.out.println(String.format("%d. %s", id, name));
        }
    }

    private void dropTable() {
        try {
            ProgramLogic.dropTable(this.scanner.nextLine());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
