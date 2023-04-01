package tracker;

import java.util.Comparator;

public class Course {

    private Students students;
    private Courses name;

    public Course(String name) {
        String lcName = name.toLowerCase();
        if (lcName.equals("java")) {
            this.name = Courses.JAVA;
        } else if (lcName.equals("dsa")) {
            this.name = Courses.DSA;
        } else if (lcName.equals("databases")) {
            this.name = Courses.DATABASES;
        } else if (lcName.equals("spring")) {
            this.name = Courses.SPRING;
        }
        this.students = new Students();
    }

    public int studentsSize() {
        return students.size();
    }

    public boolean contains(Student student) {
        return this.students.contains(student);
    }

    public void add(Student student) {
        this.students.add(student);
    }

    public String listStudents() {
        StringBuilder toReturn = new StringBuilder(this.name.toString());
        toReturn.append("id\tpoints\tcompleted");
        toReturn.append("\n");
        for (Student student : students.getStudents()) {
            toReturn.append(student.getId());
            toReturn.append("\t");
            toReturn.append(student.getJavaPts()); // points to change, obv
            toReturn.append("\n");
        }
        return toReturn.toString();
    }

    public String toString() {
        return this.name.toString();
    }
}

class CourseStudentsSizeComparator implements Comparator<Course> {

    @Override
    public int compare(Course c1, Course c2) {
        return Integer.valueOf(c1.studentsSize()).compareTo(c2.studentsSize());
    }
}

enum Courses {
    JAVA(600), DSA(400), DATABASES(480), SPRING(550);

    private int totalPts;

    Courses(int pts) {
        this.totalPts = pts;
    }

    public int getTotalPoints() {
        return this.totalPts;
    }
}
