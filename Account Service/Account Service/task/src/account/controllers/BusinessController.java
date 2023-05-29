package account.controllers;

import account.exceptions.UserNotExistsException;
import account.models.User;
import account.services.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping(value = {"/api/empl/payment", "/api/empl/payment/"})
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity getPayroll(@AuthenticationPrincipal UserDetails userDetails) {
        User user = businessService.getPayrolls(userDetails);
        return ResponseEntity.ok(user);
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
