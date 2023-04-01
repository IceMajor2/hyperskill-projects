package tracker;

import java.util.ArrayList;
import java.util.List;

public class Students {

    private List<Student> students;

    public Students() {
        this.students = new ArrayList<>();
    }

    public void add(Student student) {
        this.students.add(student);
    }

    public int size() {
        return this.students.size();
    }

    public Student get(int id) {
        for (Student student : students) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }

    public boolean isEmailInDatabase(String email) {
        for (Student student : students) {
            if (student.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public List<Student> getStudents() {
        return this.students;
    }
    
    public boolean contains(Student student) {
        return this.students.contains(student);
    }
}
