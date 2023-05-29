package account;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @GetMapping("/api/empl/payment")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT')")
    public void getPayroll() {

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
