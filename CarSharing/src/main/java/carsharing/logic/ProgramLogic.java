package carsharing.logic;

import static carsharing.CarSharing.companies;
import static carsharing.CarSharing.dbLogic;
import carsharing.entities.Company;
import java.sql.SQLException;

public class ProgramLogic {

    public ProgramLogic() {

    }

    public static void completeAdd(Company company) throws SQLException {
        companies.add(company);
        dbLogic.addCompany(company);
    }
    
    public static void dropTable(String table) throws SQLException {
        dbLogic.dropTable(table);
    }
}
