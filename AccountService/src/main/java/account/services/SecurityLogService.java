package account.services;

import account.dto.RoleDTO;
import account.dto.UserActionDTO;
import account.enums.AccountAction;
import account.enums.OperationType;
import account.enums.Roles;
import account.enums.SecurityAction;
import account.models.SecurityLog;
import account.models.User;
import account.repositories.SecurityLogRepository;
import account.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityLogService {

    private SecurityLogRepository securityLogRepository;
    private UserRepository userRepository;

    public SecurityLogService(SecurityLogRepository securityLogRepository, UserRepository userRepository) {
        this.securityLogRepository = securityLogRepository;
        this.userRepository = userRepository;
    }

    public List<SecurityLog> getLogs() {
        var logs = securityLogRepository.findAllByOrderByIdAsc();
        return logs;
    }

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

    public void saveRoleChangedLog(UserDetails adminDetails, User user, RoleDTO roleDTO) {
        OperationType op = roleDTO.getOperation();
        var action = op == OperationType.GRANT ? SecurityAction.GRANT_ROLE : SecurityAction.REMOVE_ROLE;
        String subject = adminDetails.getUsername();
        Roles role = AdminService.parseRole(roleDTO.getRole());
        String object = "%s role %s %s %s".formatted(op.inLowerCaseExceptFirst(), role.noPrefix(),
                op.getToFrom(), user.getEmail());
        String path = "/api/admin/user/role";
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogRepository.save(log);
    }

    public void saveAccountLockLog(UserDetails adminDetails, User user, UserActionDTO userActionDTO) {
        AccountAction op = userActionDTO.getOperation();
        var action = op == AccountAction.LOCK ? SecurityAction.LOCK_USER : SecurityAction.UNLOCK_USER;
        String subject = adminDetails.getUsername();
        String object = "%s user %s".formatted(op.inLowerCaseExceptFirst(), user.getEmail());
        String path = "/api/admin/user/access";
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogRepository.save(log);
    }

    public void saveAccountDeletedLog(UserDetails adminDetails, User user) {
        var action = SecurityAction.DELETE_USER;
        String subject = adminDetails.getUsername();
        String object = user.getEmail();
        String path = "/api/admin/user";
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogRepository.save(log);
    }

    public void saveLog(SecurityLog log) {
        securityLogRepository.save(log);
    }
}
