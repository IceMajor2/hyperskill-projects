package tracker;

import java.util.List;

public class Course {

    private Students students;
    private CourseType name;
    private int tasksCompleted;
    private int totalPointsGotByStudents;

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
        this.totalPointsGotByStudents = 0;
    }

    public int studentsSize() {
        return students.size();
    }
    
    public void completeTask(int points) {
        this.totalPointsGotByStudents += points;
        this.tasksCompleted++;
    }

    public int getTasksCompleted() {
        return tasksCompleted;
    }
    
    public double averageScore() {
        return 1.0 * this.totalPointsGotByStudents / this.tasksCompleted;
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
    
    public List<Student> leaders() {
        students.getStudents().sort((s1, s2) -> {
            int s1CoursePoints = s1.getPoints()[this.name.ordinal()];
            int s2CoursePoints = s2.getPoints()[this.name.ordinal()];
            if(Integer.valueOf(s1CoursePoints).compareTo(s2CoursePoints) == 0) {
                int s1Id = s1.getId();
                int s2Id = s2.getId();
                return Integer.valueOf(s2Id).compareTo(s1Id); // reversed
            } 
            return Integer.valueOf(s1CoursePoints).compareTo(s2CoursePoints);
        });
        return students.getStudents();
    }
}

enum CourseType {
    JAVA("Java", 600), DSA("DSA", 400), DATABASES("Databases", 480),
    SPRING("Spring", 550);

    private String name;
    private int totalPts;

    CourseType(String name, int pts) {
        this.totalPts = pts;
        this.name = name;
    }

    public int getTotalPoints() {
        return this.totalPts;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
