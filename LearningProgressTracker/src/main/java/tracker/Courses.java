package tracker;

import java.util.ArrayList;
import java.util.List;

public class Courses {
    
    private List<Course> courses;
    
    public Courses() {
        this.courses = new ArrayList<>();
    }
    
    public void add(Course c) {
        this.courses.add(c);
    }
    
    public Course get(int index) {
        return this.courses.get(index);
    }
    
    public Course mostPopular() {
        courses.sort(new CourseStudentsSizeComparator());
        return courses.get(courses.size() - 1);
    }
    
    public Course leastPopular() {
        courses.sort(new CourseStudentsSizeComparator());
        return courses.get(0);
    }
}
