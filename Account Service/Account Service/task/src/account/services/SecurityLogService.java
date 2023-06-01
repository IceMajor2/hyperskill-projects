package account.services;

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

    public void saveLog(SecurityLog log) {
        securityLogRepository.save(log);
    }
}
