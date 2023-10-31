package account.dto;

import account.enumerated.AccountAction;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserActionDTO {

    @NotEmpty
    @JsonProperty("user")
    private String email;
    @Enumerated(EnumType.STRING)
    private AccountAction operation;
}
