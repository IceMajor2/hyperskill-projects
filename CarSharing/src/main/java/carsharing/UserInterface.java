package carsharing;

import java.util.Scanner;

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
            if (choice.equals("1")) {
                runManager();
                continue;
            }
        }
    }

    public void runManager() {
        while (true) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");
            String choice = this.scanner.nextLine();
            
            if(choice.equals("0")) {
                break;
            }
        }
    }
}
