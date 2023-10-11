package account.exceptions.roles;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "The user does not have a role!")
public class RoleNotFoundException extends RuntimeException {
}
