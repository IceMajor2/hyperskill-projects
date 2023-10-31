package account.security;

import account.enumerated.SecurityAction;
import account.model.SecurityLog;
import account.service.SecurityLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final SecurityLogService securityLogService;

    @Override
    public void handle(HttpServletRequest request,
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
