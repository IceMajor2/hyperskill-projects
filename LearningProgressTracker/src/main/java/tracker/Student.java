package tracker;

import java.math.BigDecimal;
import java.math.RoundingMode;
import static tracker.Main.*;

public class Student {

    public static int studentCount = 0;
    private String firstName;
    private String lastName;
    private String email;
    private int id;
    private int[] points;

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = studentCount + 1;
        this.points = new int[courses.size()];

        studentCount++;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }

        if(!(o instanceof Student)) {
            return false;
        }

        Student obj = (Student) o;

        if(obj.email.equals(this.email)) {
            return true;
        }
        return false;
    }

    public int[] getPoints() {
        return points;
    }
    
    public String getPercentCompletion(int courseOrdinal) {
        int points = this.getPoints()[courseOrdinal];
        int totalPoints = courses.get(courseOrdinal).getName().getTotalPoints();
        
        double percent = (double) points / totalPoints * 100;
        BigDecimal bd = BigDecimal.valueOf(percent);
        bd = bd.setScale(1, RoundingMode.HALF_UP);
        
        return bd + "%";
    }

    public void addPoints(int java, int dsa, int databases, int spring) {
        this.points[CourseType.JAVA.ordinal()] += java;
        this.points[CourseType.DSA.ordinal()] += dsa;
        this.points[CourseType.DATABASES.ordinal()] += databases;
        this.points[CourseType.SPRING.ordinal()] += spring;
    }
}
