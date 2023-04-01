package tracker;

import java.util.ArrayList;
import java.util.Comparator;
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
        courses.sort((c1, c2) -> {
            return Integer.valueOf(c1.getName().ordinal())
                    .compareTo(c2.getName().ordinal());
        });
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
    
    public Course mostActive() {
        courses.sort(new CourseCompletedTasksComparator());
        return courses.get(courses.size() - 1);
    }
    
    public Course leastActive() {
        courses.sort(new CourseCompletedTasksComparator());
        return courses.get(0);
    }
    
    public Course easiest() {
        courses.sort(new CourseAvgScoreComparator());
        return courses.get(courses.size() - 1);
    }
    
    public Course hardest() {
        courses.sort(new CourseAvgScoreComparator());
        return courses.get(0);
    }
}

class CourseStudentsSizeComparator implements Comparator<Course> {

    @Override
    public int compare(Course c1, Course c2) {
        return Integer.valueOf(c1.studentsSize()).compareTo(c2.studentsSize());
    }
}

class CourseCompletedTasksComparator implements Comparator<Course> {
    
    @Override
    public int compare(Course c1, Course c2) {
        return Integer.valueOf(c1.getTasksCompleted())
                .compareTo(c2.getTasksCompleted());
    }
}

class CourseAvgScoreComparator implements Comparator<Course> {
    
    @Override
    public int compare(Course c1, Course c2) {
        return Double.valueOf(c1.averageScore()).compareTo(c2.averageScore());
    }
}
