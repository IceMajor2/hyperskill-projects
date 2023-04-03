package tracker;

import java.util.Scanner;
import java.util.List;

import static tracker.Main.*;

public class UserInterface {

    private final Scanner scanner;

    public UserInterface() {
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        //createTestDB();
        while (true) {
            String usrCommand = scanner.nextLine();
            if ("exit".equals(usrCommand)) {
                System.out.println("Bye!");
                break;
            }
            if ("back".equals(usrCommand)) {
                System.out.println("Enter 'exit' to exit the program.");
                continue;
            }
            if (usrCommand.isBlank()) {
                System.out.println("No input.");
                continue;
            }
            if ("add students".equals(usrCommand)) {
                System.out.println("Enter student credentials or 'back' to return:");
                addStudents();
                continue;
            }
            if ("add points".equals(usrCommand)) {
                System.out.println("Enter an id and points or 'back' to return:");
                addPoints();
                continue;
            }
            if ("list".equals(usrCommand)) {
                listStudents();
                continue;
            }
            if ("find".equals(usrCommand)) {
                System.out.println("Enter an id or 'back' to return:");
                outputStudent();
                continue;
            }
            if ("statistics".equals(usrCommand)) {
                System.out.println("Type the name of a course to see details"
                        + " or 'back' to quit:");
                statistics();
                continue;
            }
            if ("notify".equals(usrCommand)) {
                notifyStudents();
                continue;
            }
            System.out.println("Error: unknown command!");
        }
    }

    private void statistics() {
        printStatistics();
        while (true) {
            String usrCommand = scanner.nextLine();
            usrCommand = usrCommand.toLowerCase();
            if ("back".equals(usrCommand)) {
                break;
            }
            if ("java".equals(usrCommand)) {
                printCourseDetails(usrCommand);
                continue;
            }
            if ("dsa".equals(usrCommand)) {
                printCourseDetails(usrCommand);
                continue;
            }
            if ("databases".equals(usrCommand)) {
                printCourseDetails(usrCommand);
                continue;
            }
            if ("spring".equals(usrCommand)) {
                printCourseDetails(usrCommand);
                continue;
            }
            System.out.println("Unknown course.");
        }

    }

    private void printCourseDetails(String courseName) {
        CourseType course = CourseType.valueOf(courseName.toUpperCase());
        System.out.println(course);
        System.out.println("id\tpoints\tcompleted");
        List<Student> leaders = courses.get(course.ordinal()).leaders();
        for (int i = leaders.size() - 1; i >= 0; i--) {
            Student student = leaders.get(i);
            System.out.println(student.getId() + "\t"
                    + student.getPoints()[course.ordinal()] + "\t"
                    + student.getPercentCompletion(course.ordinal()));
        }
    }

    private void printStatistics() {
        if (students.size() == 0) {
            System.out.println("Most popular: n/a\n"
                    + "Least popular: n/a\n"
                    + "Highest activity: n/a\n"
                    + "Lowest activity: n/a\n"
                    + "Easiest course: n/a\n"
                    + "Hardest course: n/a");
            return;
        }
        var mostPopular = courses.mostPopular();
        var leastPopular = courses.leastPopular();
        var mostActive = courses.mostActive();
        var leastActive = courses.leastActive();
        var easiest = courses.easiest();
        var hardest = courses.hardest();

        System.out.println("Most popular: " + new StringBuilder(Main.reverse(mostPopular).toString()
                .substring(1, mostPopular.toString().length() - 1)));
        if (leastPopular != null) {
            System.out.println("Least popular: " + new StringBuilder(leastPopular.toString()
                    .substring(1, leastPopular.toString().length() - 1)));
        } else {
            System.out.println("Least popular: n/a");
        }
        System.out.println("Highest activity: " + new StringBuilder(Main.reverse(mostActive).toString()
                .substring(1, mostActive.toString().length() - 1)));
        if (leastActive != null) {
            System.out.println("Lowest activity: " + new StringBuilder(leastActive.toString()
                    .substring(1, leastActive.toString().length() - 1)));
        } else {
            System.out.println("Least active: n/a");
        }
        System.out.println("Easiest course: " + new StringBuilder(Main.reverse(easiest).toString()
                .substring(1, easiest.toString().length() - 1)));
        if (hardest != null) {
            System.out.println("Hardest course: " + new StringBuilder(hardest.toString()
                    .substring(1, hardest.toString().length() - 1)));
        } else {
            System.out.println("Hardest course: n/a");
        }
    }

    private void outputStudent() {
        while (true) {
            String input = scanner.nextLine();
            if ("back".equals(input)) {
                break;
            }
            int id = -1;
            try {
                id = Integer.valueOf(input);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect ID format.");
            }
            Student student = students.get(id);
            if (student == null) {
                System.out.println("No student is found for id=" + id);
                continue;
            }
            System.out.println(student);
        }
    }

    private void listStudents() {
        if (students.size() == 0) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("Students:");
        for (Student student : students.getStudents()) {
            System.out.println(student.getId());
        }
    }

    private void addStudents() {
        String nameRegex = "[A-Za-z]+(-|')?[A-Za-z]+((-|')[A-Za-z])?";
        String emailRegex = "[A-Za-z_.0-9-]+@[A-Za-z_.0-9-]+\\.[A-Za-z_.0-9-]+";

        int counter = 0;
        one:
        while (true) {
            String credentials = scanner.nextLine();
            if ("back".equals(credentials)) {
                break;
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
            if (students.isEmailInDatabase(email)) {
                System.out.println("This email is already taken.");
                continue;
            }
            String[] lastNameParts = new String[pieces.length - 2];
            StringBuilder lastName = new StringBuilder();
            for (int i = 0; i < pieces.length - 2; i++) {
                lastNameParts[i] = pieces[i + 1];

                if (!lastNameParts[i].matches(nameRegex)) {
                    System.out.println("Incorrect last name.");
                    continue one;
                }
                lastName.append(lastNameParts[i]);
            }
            if (!email.matches(emailRegex)) {
                System.out.println("Incorrect email.");
                continue;
            }
            Student student = new Student(firstName, lastName.toString(), email);
            students.add(student);
            System.out.println("The student has been added.");
            counter++;
        }
        System.out.println("Total " + counter + " students have been added.");
    }

    private void addPoints() {
        // String inputRegex = "//d+ //d+ //d+ //d+ //d+";

        while (true) {
            String input = scanner.nextLine();
            if ("back".equals(input)) {
                break;
            }
            String[] pieces = input.split(" ");
            int id = -1;
            try {
                id = Integer.valueOf(pieces[0]);
            } catch (NumberFormatException e) {
                System.out.println("No student is found for id=" + pieces[0] + ".");
                continue;
            }
            Student student = students.get(id);
            if (student == null) {
                System.out.println("No student is found for id=" + id + ".");
                continue;
            }
            if (pieces.length != 5) {
                System.out.println("Incorrect points format.");
                continue;
            }
            int javaPts = -1, dsaPts = -1, databasesPts = -1, springPts = -1;
            try {
                javaPts = Integer.valueOf(pieces[1]);
                dsaPts = Integer.valueOf(pieces[2]);
                databasesPts = Integer.valueOf(pieces[3]);
                springPts = Integer.valueOf(pieces[4]);
            } catch (NumberFormatException e) {
                System.out.println("Incorrect points format.");
                continue;
            }
            if (javaPts < 0 || dsaPts < 0 || databasesPts < 0 || springPts < 0) {
                System.out.println("Incorrect points format.");
                continue;
            }
            student.addPoints(javaPts, dsaPts, databasesPts, springPts);
            addStudentToCourses(student);
            incrementCompletedTasks(javaPts, dsaPts, databasesPts, springPts);
            System.out.println("Points updated.");
        }
    }

    private void addStudentToCourses(Student student) {
        if (student.getPoints()[CourseType.JAVA.ordinal()] > 0) {
            Course java = courses.get(CourseType.JAVA.ordinal());
            if (!java.contains(student)) {
                java.add(student);
            }
        }
        if (student.getPoints()[CourseType.DSA.ordinal()] > 0) {
            Course dsa = courses.get(CourseType.DSA.ordinal());
            if (!dsa.contains(student)) {
                dsa.add(student);
            }
        }
        if (student.getPoints()[CourseType.DATABASES.ordinal()] > 0) {
            Course databases = courses.get(CourseType.DATABASES.ordinal());
            if (!databases.contains(student)) {
                databases.add(student);
            }
        }
        if (student.getPoints()[CourseType.SPRING.ordinal()] > 0) {
            Course spring = courses.get(CourseType.SPRING.ordinal());
            if (!spring.contains(student)) {
                spring.add(student);
            }
        }
    }

    private void incrementCompletedTasks(int javaPts, int dsaPts, int databasesPts, int springPts) {
        if (javaPts > 0) {
            courses.get(0).completeTask(javaPts);
        }
        if (dsaPts > 0) {
            courses.get(1).completeTask(dsaPts);
        }
        if (databasesPts > 0) {
            courses.get(2).completeTask(databasesPts);
        }
        if (springPts > 0) {
            courses.get(3).completeTask(springPts);
        }
    }

    private void createTestDB() {
        students.add(new Student("John", "White", "jw@ny.com"));
        students.add(new Student("Winston", "Smith", "orwell1984@gmail.com"));
        students.add(new Student("George", "Carlin", "gc@ca.com"));

        students.get(1).addPoints(0, 4, 0, 4);
        incrementCompletedTasks(0, 4, 0, 4);
        addStudentToCourses(students.get(1));

        students.get(2).addPoints(4, 4, 4, 0);
        incrementCompletedTasks(4, 4, 4, 0);
        addStudentToCourses(students.get(2));

        students.get(3).addPoints(4, 0, 4, 4);
        incrementCompletedTasks(4, 0, 4, 4);
        addStudentToCourses(students.get(3));

        /*students.get(3).addPoints(7, 0, 0, 0);
        incrementCompletedTasks(7, 0, 0, 0);
        addStudentToCourses(students.get(3));

        students.get(1).addPoints(0, 2, 0, 0);
        incrementCompletedTasks(0, 2, 0, 0);
        students.get(1).addPoints(0, 1, 0, 7);
        incrementCompletedTasks(0, 1, 0, 7);*/
    }

    private void notifyStudents() {
        int newGraduatesTotal = 0;
        Students allNewGraduates = new Students();
        for (Course course : courses.getCourses()) {
            Students newGraduates = course.newGraduates();
            for (Student graduate : newGraduates.getStudents()) {
                printCongratulations(graduate.getEmail(),
                        graduate.getFirstName(), graduate.getLastName(), course);
                if (!allNewGraduates.contains(graduate)) {
                    newGraduatesTotal++;
                }
                allNewGraduates.add(graduate);
            }
        }
        System.out.println("Total " + newGraduatesTotal
                + " students have been notified.");
    }

    private void printCongratulations(String email, String firstName, String lastName, Course course) {
        System.out.println("To: " + email);
        System.out.println("Re: Your Learning Progress");
        System.out.println("Hello, " + firstName + " " + lastName + "!"
                + " You have accomplished our " + course.getName() + " course!");
    }
}
