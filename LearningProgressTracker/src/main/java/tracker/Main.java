package tracker;

import java.util.List;

public class Main {

    public static Students students = new Students();
    public static Courses courses = new Courses();
    
    /*  TODO:
            
    */

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
    
    public static List<Course> reverse(List<Course> list) {
        list.sort((c1, c2) -> {
            return Integer.valueOf(c1.getName().ordinal())
                    .compareTo(c2.getName().ordinal());
        });
        return list;
    }
}
