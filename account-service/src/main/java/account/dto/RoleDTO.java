package account.dto;

import account.enumerated.OperationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {

    @JsonProperty("user")
    @NotEmpty
    private String email;
    private String role;
    @Enumerated(EnumType.STRING)
    private OperationType operation;
}
