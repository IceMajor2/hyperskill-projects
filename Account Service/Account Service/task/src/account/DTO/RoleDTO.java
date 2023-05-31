package account.DTO;

import account.enums.OperationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;

public class RoleDTO {

    @JsonProperty("user")
    @NotEmpty
    private String email;
    private String role;
    @Enumerated(EnumType.STRING)
    private OperationType operation;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }
}
