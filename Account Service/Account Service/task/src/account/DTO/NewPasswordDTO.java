package account.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {

    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    @NotEmpty
    @JsonProperty("new_password")
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
