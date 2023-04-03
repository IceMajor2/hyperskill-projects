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

    public int size() {
        return this.courses.size();
    }

    public List<Course> mostPopular() {
        courses.sort(new CourseStudentsSizeComparator());
        List<Course> mostPopular = new ArrayList<>();
        mostPopular.add(courses.get(courses.size() - 1));
        for(int i = courses.size() - 2; i >= 0; i--) {
            Course course = courses.get(i);
            if(course.studentsSize() != mostPopular.get(0).studentsSize()) {
                return mostPopular;
            }
            mostPopular.add(course);
        }
        return mostPopular;
    }

    public List<Course> leastPopular() {
        courses.sort(new CourseStudentsSizeComparator());
        List<Course> leastPopular = new ArrayList<>();
        leastPopular.add(courses.get(0));
        for(int i = 1; i < courses.size(); i++) {
            Course course = courses.get(i);
            if(course.studentsSize() != leastPopular.get(0).studentsSize()) {
                return leastPopular;
            }
            leastPopular.add(course);
        }
        if(leastPopular.containsAll(courses)) {
            return null;
        }
        return leastPopular;
    }

    public List<Course> mostActive() {
        courses.sort(new CourseCompletedTasksComparator());
        List<Course> mostActive = new ArrayList<>();
        mostActive.add(courses.get(courses.size() - 1));
        for(int i = courses.size() - 2; i >= 0; i--) {
            Course course = courses.get(i);
            if(course.getTasksCompleted() != mostActive.get(0).getTasksCompleted()) {
                return mostActive;
            }
            mostActive.add(course);
        }
        return mostActive;
    }

    public List<Course> leastActive() {
        courses.sort(new CourseCompletedTasksComparator());
        List<Course> leastActive = new ArrayList<>();
        leastActive.add(courses.get(0));
        for(int i = 1; i < courses.size(); i++) {
            Course course = courses.get(i);
            if(course.getTasksCompleted() != leastActive.get(0).getTasksCompleted()) {
                return leastActive;
            }
            leastActive.add(course);
        }
        if(leastActive.containsAll(courses)) {
            return null;
        }
        return leastActive;
    }

    public List<Course> easiest() {
        courses.sort(new CourseAvgScoreComparator());
        List<Course> easiest = new ArrayList<>();

        int index = courses.size() - 1;
        while(courses.get(index).averageScore() == -1) {
            index--;
        }

        easiest.add(courses.get(index));
        for(int i = index - 1; i >= 0; i--) {
            Course course = courses.get(i);
            if(course.averageScore() != easiest.get(0).averageScore()) {
                return easiest;
            }
            easiest.add(course);
        }
        return easiest;
    }

    public List<Course> hardest() {
        courses.sort(new CourseAvgScoreComparator());
        List<Course> hardest = new ArrayList<>();

        int index = 0;
        while(courses.get(index).averageScore() == -1) {
            index++;
        }

        hardest.add(courses.get(index));
        for(int i = index + 1; i < courses.size(); i++) {
            Course course = courses.get(i);
            if(course.averageScore() != hardest.get(0).averageScore()) {
                return hardest;
            }
            hardest.add(course);
        }
        if(hardest.isEmpty() || hardest.containsAll(easiest())) {
            return null;
        }
        return hardest;
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
