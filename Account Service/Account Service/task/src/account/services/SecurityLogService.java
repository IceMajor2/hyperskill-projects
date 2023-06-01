package account.services;

import account.DTO.RoleDTO;
import account.enums.OperationType;
import account.enums.Roles;
import account.enums.SecurityAction;
import account.models.SecurityLog;
import account.models.User;
import account.repositories.SecurityLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityLogService {

    @Autowired
    private SecurityLogRepository securityLogRepository;

    public void saveCreateUserLog(User user) {
        var action = SecurityAction.CREATE_USER;
        String subject = "Anonymous";
        String object = user.getEmail();
        String path = "/api/auth/signup";
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogRepository.save(log);
    }

    public void saveChangePasswordLog(User user) {
        var action = SecurityAction.CHANGE_PASSWORD;
        String subject = user.getEmail();
        String object = user.getEmail();
        String path = "/api/auth/changepass";
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogRepository.save(log);
    }

    public void saveRoleChangedLog(User user, RoleDTO roleDTO) {
        OperationType op = roleDTO.getOperation();
        var action = op == OperationType.GRANT ? SecurityAction.GRANT_ROLE : SecurityAction.REMOVE_ROLE;
        String subject = user.getEmail();
        Roles role = AdminService.parseRole(roleDTO.getRole());
        String object = "%s role %s to %s".formatted(op.inLowerCaseExceptFirst(), role.noPrefix(), subject);
        String path = "/api/adming/user/role";
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogRepository.save(log);
    }

    public void saveLog(SecurityLog log) {
        securityLogRepository.save(log);
    }
}
