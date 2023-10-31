package account.dto;

import account.model.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
public class AuthPaymentDTO {

    private String name;
    private String lastname;
    private String period;
    private String salary;

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
