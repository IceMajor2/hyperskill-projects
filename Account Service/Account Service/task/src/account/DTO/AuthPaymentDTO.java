package account.DTO;

public class AuthPaymentDTO {

    private String name;
    private String lastname;
    private String period;
    private String salary;

    public AuthPaymentDTO() {
    }

    public AuthPaymentDTO(String name, String lastname, String period, Long salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = period;
        this.salary = this.format(salary);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    private String format(Long salary) {
        long copy = salary;

        int dollars = (int) (copy / 100);
        copy -= dollars * 100;
        int cents = (int) copy;
        return "%d dollar(s) %d cent(s)".formatted(dollars, cents);
    }
}
