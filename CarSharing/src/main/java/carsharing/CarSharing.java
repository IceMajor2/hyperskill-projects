package carsharing;

import carsharing.logic.DBLogic;
import java.sql.SQLException;

public class CarSharing {
    
    public static void main(String[] args) {
        DBLogic dbLogic = null;
        try {
            dbLogic = new DBLogic();
            dbLogic.executeQuery("ALTER TABLE company "
                + "CHANGE id id INTEGER AUTO_INCREMENT PRIMARY KEY,"
                + "MODIFY name VARCHAR(30) NOT NULL,"
                + "ADD UNIQUE (name);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserInterface UI = new UserInterface();
    }
}
