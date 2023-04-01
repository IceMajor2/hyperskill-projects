package tracker;

public class Student {

    public static int studentCount = 0;
    private String firstName;
    private String lastName;
    private String email;
    private int id;
    private int javaPts;
    private int dsaPts;
    private int databasesPts;
    private int springPts;

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.id = studentCount + 1;

        this.javaPts = 0;
        this.databasesPts = 0;
        this.dsaPts = 0;
        this.springPts = 0;

        studentCount++;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return this.email;
    }

    /*public boolean equals(Object o) {
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
    }*/

    public void addPoints(int java, int dsa, int databases, int spring) {
        this.javaPts += java;
        this.dsaPts += dsa;
        this.databasesPts += databases;
        this.springPts += spring;
    }

    public String toString() {
        return this.id + " points: " + "Java=" + javaPts + "; DSA=" + dsaPts + "; Databases=" + databasesPts
                + "; Spring=" + springPts;
    }

}
