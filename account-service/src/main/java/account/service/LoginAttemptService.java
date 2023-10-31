package account.service;

import account.enumerated.SecurityAction;
import account.model.SecurityLog;
import account.model.User;
import account.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 4;
    private Map<String, Integer> attempts;
    private SecurityLogService securityLogService;
    private UserRepository userRepository;

    public LoginAttemptService(SecurityLogService securityLogService, UserRepository userRepository) {
        super();
        this.securityLogService = securityLogService;
        this.userRepository = userRepository;
        attempts = new HashMap<>();
    }

    public void loginFailed(final String userEmail, String URI) {
        saveLoginFailedLog(userEmail, URI);
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(userEmail);

        if(userOpt.isEmpty()) {
            return;
        }
        User user = userOpt.get();
        if(user.isAdmin()) {
            return;
        }
        if(!user.isAccountNonLocked()) {
            return;
        }

        int usrAttempts = attempts.getOrDefault(userEmail, 0);
        usrAttempts++;
        attempts.put(userEmail, usrAttempts);
        System.out.println(usrAttempts);
        if(usrAttempts > MAX_ATTEMPTS) {
            saveBruteForceLog(userEmail, URI);
            user.setAccountNonLocked(false);
            saveLockUserLog(userEmail, URI);
            userRepository.save(user);
        }
    }

    public void loginSuccessful(final String userEmail) {
        attempts.put(userEmail, 0);
    }

    private void saveLoginFailedLog(final String userEmail, String URI) {
        var action = SecurityAction.LOGIN_FAILED;
        String subject = userEmail;
        String object = URI;
        String path = URI;
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogService.saveLog(log);
    }

    private void saveBruteForceLog(final String userEmail, String URI) {
        var action = SecurityAction.BRUTE_FORCE;
        String subject = userEmail;
        String object = URI;
        String path = URI;
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogService.saveLog(log);
    }

    private void saveLockUserLog(final String userEmail, String URI) {
        var action = SecurityAction.LOCK_USER;
        String subject = userEmail;
        String object = "Lock user %s".formatted(userEmail);
        String path = URI;
        SecurityLog log = new SecurityLog(action, subject, object, path);

        securityLogService.saveLog(log);
    }

    @Bean
    public Map<String, Integer> getAttempts() {
        return attempts;
    }
}
