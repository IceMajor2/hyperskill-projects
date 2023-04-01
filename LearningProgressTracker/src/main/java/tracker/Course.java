package tracker;

public class Course {

    private Students students;
    private CourseType name;
    private int tasksCompleted;

    public Course(String name) {
        String lcName = name.toLowerCase();
        if (lcName.equals("java")) {
            this.name = CourseType.JAVA;
        } else if (lcName.equals("dsa")) {
            this.name = CourseType.DSA;
        } else if (lcName.equals("databases")) {
            this.name = CourseType.DATABASES;
        } else if (lcName.equals("spring")) {
            this.name = CourseType.SPRING;
        }
        this.students = new Students();
        this.tasksCompleted = 0;
    }

    public int studentsSize() {
        return students.size();
    }
    
    public void completeTask() {
        this.tasksCompleted++;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }

    public boolean contains(Student student) {
        return this.students.contains(student);
    }

    public void add(Student student) {
        this.students.add(student);
    }

    public CourseType getName() {
        return name;
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

enum CourseType {
    JAVA(600), DSA(400), DATABASES(480), SPRING(550);

    private int totalPts;

    CourseType(int pts) {
        this.totalPts = pts;
    }

    public int getTotalPoints() {
        return this.totalPts;
    }
}
