package account.controllers;

import account.models.User;
import account.services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("/api/empl/payment")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity getPayroll(@AuthenticationPrincipal User details) {
        return ResponseEntity.ok(Map.of("id", details.getId(),
                "name", details.getName(),
                "lastname", details.getLastName(),
                "email", details.getEmail()));
        //userRepository.findByEmailIgnoreCase(details.getUsername());
    }

    @PostMapping("/api/acct/payments")
    @PreAuthorize("hasAuthority('ROLE_ACCOUNTANT')")
    public void uploadPayroll() {

    }

    @PutMapping("/api/acct/payments")
    @PreAuthorize("hasAuthority('ROLE_ACCOUNTANT')")
    public void updatePayment() {

    }
}
