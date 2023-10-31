package account.controller;

import account.service.SecurityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

    @Autowired
    private SecurityLogService securityLogService;

    @GetMapping(value = {"/events", "/events/"})
    @PreAuthorize("hasAuthority('ROLE_AUDITOR')")
    public ResponseEntity getLogs() {
        var logs = securityLogService.getLogs();
        return ResponseEntity.ok(logs);
    }
}
