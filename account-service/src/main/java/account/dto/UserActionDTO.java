package account.dto;

import account.enums.AccountAction;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;

public class UserActionDTO {

    @NotEmpty
    @JsonProperty("user")
    private String email;
    @Enumerated(EnumType.STRING)
    private AccountAction operation;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountAction getOperation() {
        return operation;
    }

    public void setOperation(AccountAction operation) {
        this.operation = operation;
    }
}
