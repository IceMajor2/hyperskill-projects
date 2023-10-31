package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

    @Pattern(regexp = ".+@acme.com\\b", message = "Address e-mail not valid!")
    @JsonProperty("employee")
    private String email;
    @Pattern(regexp = "\\b(0[1-9]|1[0-2])-(19[0-9]{2}|20[0-9]{2})\\b", message = "Date format not valid!")
    private String period;
    @PositiveOrZero(message = "Salary cannot be negative!")
    private Long salary;
}
