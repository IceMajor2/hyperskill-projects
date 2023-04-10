package carsharing.logic;

import static carsharing.CarSharing.companies;
import static carsharing.CarSharing.dbLogic;

import java.util.stream.Collectors;
import java.sql.SQLException;

import carsharing.Company;

public class ProgramLogic {

    public ProgramLogic() {

    }

    public static void orderCompaniesById() {
        var sorted = companies.getCompanies().stream().sorted((c1, c2) -> {
            return Integer.valueOf(c1.getId()).compareTo(c2.getId());
        }).collect(Collectors.toList());
        companies.setCompanies(sorted);
    }

    public static void completeAdd(Company company) throws SQLException {
        companies.add(company);
        dbLogic.addCompany(company);
    }
    
    public static void dropTable(String table) throws SQLException {
        dbLogic.dropTable(table);
    }
}
