package carsharing;

import carsharing.logic.DBLogic;
import java.sql.SQLException;

public class CarSharing {

    public static Companies companies = new Companies();
    public static DBLogic dbLogic = null;
    
    public static void main(String[] args) {
        try {
            dbLogic = new DBLogic();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserInterface UI = new UserInterface();
        UI.run();
    }
}
