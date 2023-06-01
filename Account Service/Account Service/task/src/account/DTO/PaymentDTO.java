package account.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class PaymentDTO {

    @Pattern(regexp = ".+@acme.com\\b", message = "Address e-mail not valid!")
    @JsonProperty("employee")
    private String email;

    @Pattern(regexp = "\\b(0[1-9]|1[0-2])-(19[0-9]{2}|20[0-9]{2})\\b", message = "Date format not valid!")
    private String period;

    @PositiveOrZero(message = "Salary cannot be negative!")
    private Long salary;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }
}
