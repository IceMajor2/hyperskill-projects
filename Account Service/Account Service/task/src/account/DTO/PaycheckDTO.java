package account.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PaycheckDTO {

    @Pattern(regexp = ".+@acme.com\\b", message = "Address e-mail not valid!")
    private String email;
    @DateTimeFormat(pattern = "mm-YYYY")
    private LocalDate period;
    @PositiveOrZero
    private Long salary;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
