package account.DTO;

import account.models.Payment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AuthPaymentDTO {

    private String name;
    private String lastname;
    private String period;
    private String salary;

    public AuthPaymentDTO() {
    }

    public AuthPaymentDTO(String name, String lastname, Date period, Long salary) {
        this.name = name;
        this.lastname = lastname;
        this.period = this.format(period);
        this.salary = this.format(salary);
    }

    public AuthPaymentDTO(Payment payment) {
        this.name = payment.getUser().getName();
        this.lastname = payment.getUser().getLastName();
        this.period = this.format(payment.getPeriod());
        this.salary = this.format(payment.getSalary());
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

    private String format(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy", Locale.US);
        return dateFormat.format(date);
    }
}
