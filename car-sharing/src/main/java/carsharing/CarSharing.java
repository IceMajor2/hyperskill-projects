package carsharing;

import carsharing.entities.Companies;
import carsharing.entities.Cars;
import carsharing.logic.DBLogic;
import java.sql.SQLException;

public class CarSharing {

    public static Companies companies = new Companies();
    public static Cars cars = new Cars();
    public static DBLogic dbLogic = null;
    
    public static void main(String[] args) {
        try {
            dbLogic = new DBLogic();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserInterface UI = new UserInterface();
        UI.startMenu();
    }
}
