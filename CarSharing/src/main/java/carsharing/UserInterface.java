package carsharing;

import static carsharing.CarSharing.companies;
import carsharing.logic.ProgramLogic;
import java.util.Scanner;
import java.sql.SQLException;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            System.out.println("1. Log in as a manager");
            System.out.println("0. Exit");
            String choice = this.scanner.nextLine();

            if (choice.equals("0")) {
                break;
            }
            if(choice.equals("drop")) {
                dropTable();
                continue;
            }
            if (choice.equals("1")) {
                runManager();
                continue;
            }
        }
    }

    private void runManager() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            String choice = this.scanner.nextLine();

            if (choice.equals("0")) {
                break;
            }
            if (choice.equals("1")) {
                printCompanies();
                continue;
            }
            if (choice.equals("2")) {
                createCompany();
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
    
    private void printCompanies() {
        if(companies.size() == 0) {
            System.out.println("The company list is empty!");
            return;
        }
        System.out.println("Company list:");
        
        for(Company company : companies.getCompanies()) {
            int id = company.getId();
            String name = company.getName();
            System.out.println(String.format("%d. %s", id, name));
        }
    }
    
    private void dropTable() {
        try {
            ProgramLogic.dropTable(this.scanner.nextLine());
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
