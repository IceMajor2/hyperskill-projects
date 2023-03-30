package tracker;

import java.util.Scanner;

public class UserInterface {

    private Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            String usrCommand = scanner.nextLine();
            if ("exit".equals(usrCommand)) {
                System.out.println("Bye!");
                break;
            }
            if("back".equals(usrCommand)) {
                System.out.println("Enter 'exit' to exit the program.");
                continue;
            }
            if (usrCommand.isBlank()) {
                System.out.println("No input.");
                continue;
            }
            if ("add students".equals(usrCommand)) {
                System.out.println("Enter student credentials or 'back' to return:");
                int counter = addStudents();
                System.out.println("Total " + counter + " students have been added.");
                continue;
            }
            System.out.println("Error: unknown command!");
        }
    }

    private int addStudents() {
        String nameRegex = "[A-Za-z]+(-|')?[A-Za-z]+((-|')[A-Za-z])?";
        String emailRegex = "[A-Za-z_.0-9-]+@[A-Za-z_.0-9-]+\\.[A-Za-z_.0-9-]+";

        int counter = 0;
        one:
        while (true) {
            String credentials = scanner.nextLine();
            if ("back".equals(credentials)) {
                return counter;
            }
            String[] pieces = credentials.split(" ");
            if (pieces.length < 3) {
                System.out.println("Incorrect credentials.");
                continue;
            }
            String firstName = pieces[0];
            if (!firstName.matches(nameRegex)) {
                System.out.println("Incorrect first name.");
                continue;
            }
            String email = pieces[pieces.length - 1];
            String[] lastNameParts = new String[pieces.length - 2];
            for (int i = 0; i < pieces.length - 2; i++) {
                lastNameParts[i] = pieces[i + 1];

                if (!lastNameParts[i].matches(nameRegex)) {
                    System.out.println("Incorrect last name.");
                    continue one;
                }
            }
            if (!email.matches(emailRegex.toString())) {
                System.out.println("Incorrect email.");
                continue;
            }
            System.out.println("The student has been added.");
            counter++;
        }
    }
}
