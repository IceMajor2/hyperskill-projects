package account.security;

import account.enums.SecurityAction;
import account.models.SecurityLog;
import account.services.SecurityLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private SecurityLogService securityLogService;

    @Autowired
    public CustomAccessDeniedHandler(SecurityLogService securityLogService) {
        this.securityLogService = securityLogService;
    }

    public CustomAccessDeniedHandler() {
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException {

        response.sendError(403, "Access Denied!");

        var action = SecurityAction.ACCESS_DENIED;
        String subject = SecurityContextHolder.getContext().getAuthentication().getName();
        String object = request.getRequestURI();
        String path = request.getRequestURI();
        SecurityLog log = new SecurityLog(action, subject, object, path);
        securityLogService.saveLog(log);
    }
}
