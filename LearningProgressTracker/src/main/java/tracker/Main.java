package tracker;

import java.util.List;
import java.util.ArrayList;

public class Main {

    public static Students students;
    public static List<Course> courses = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        initializeCourses();
        UserInterface UI = new UserInterface();
        UI.run();
    }

    public static void initializeCourses() {
        courses.add(new Course("java"));
        courses.add(new Course("dsa"));
        courses.add(new Course("databases"));
        courses.add(new Course("spring"));
    }
}
