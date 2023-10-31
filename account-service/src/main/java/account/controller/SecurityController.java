package account.controller;

import account.model.SecurityLog;
import account.service.SecurityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityLogService securityLogService;

    @GetMapping(value = {"/events", "/events/"})
    @PreAuthorize("hasAuthority('ROLE_AUDITOR')")
    public List<SecurityLog> getLogs() {
        return securityLogService.getLogs();
    }
}
