package account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordDTO {

    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @NotEmpty
    @JsonProperty("new_password")
    private String password;
}
