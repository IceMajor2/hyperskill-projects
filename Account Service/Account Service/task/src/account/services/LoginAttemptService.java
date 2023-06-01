package account.services;

import account.enums.SecurityAction;
import account.models.SecurityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptService {

    @Autowired
    private SecurityLogService securityLogService;

    public void loginFailed(final String userEmail, String URI) {
        var action = SecurityAction.LOGIN_FAILED;
        String subject = userEmail;
        String object = URI;
        String path = URI;
        SecurityLog log = new SecurityLog(action, subject, object, path);
        securityLogService.saveLog(log);
    }
}
