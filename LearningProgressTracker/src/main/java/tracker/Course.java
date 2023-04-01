package tracker;

public class Course {

}

enum CourseNames { 
    JAVA(600), DSA(400), DATABASES(480), SPRING(550);
    
    private int totalPts;
    
    CourseNames(int pts) {
        this.totalPts = pts;
    }
}
