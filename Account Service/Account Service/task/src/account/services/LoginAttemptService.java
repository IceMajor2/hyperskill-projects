package account.services;

import account.enums.SecurityAction;
import account.models.SecurityLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private Map<String, Integer> attempts;
    @Autowired
    private SecurityLogService securityLogService;

    public LoginAttemptService() {
        super();
        attempts = new HashMap<>();
    }

    public void loginFailed(final String userEmail, String URI) {
        saveLogWhenLoginFailed(userEmail, URI);
        int usrAttempts = attempts.getOrDefault(userEmail, 0);
        usrAttempts++;
        attempts.put(userEmail, usrAttempts);
    }

    public void loginSuccessful(final String userEmail) {
        attempts.put(userEmail, 0);
    }

    public boolean isBlocked(final String userEmail) {
        return attempts.getOrDefault(userEmail, 0) > MAX_ATTEMPTS;
    }

    private void saveLogWhenLoginFailed(final String userEmail, String URI) {
        var action = SecurityAction.LOGIN_FAILED;
        String subject = userEmail;
        String object = URI;
        String path = URI;
        SecurityLog log = new SecurityLog(action, subject, object, path);
        securityLogService.saveLog(log);
    }
}
