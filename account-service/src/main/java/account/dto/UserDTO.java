package account.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @NotEmpty
    @Pattern(regexp = ".+@acme.com\\b", message = "Address e-mail not valid!")
    private String email;
    @NotEmpty
    @Size(min = 12, message = "Password length must be 12 chars minimum!")
    private String password;
}
