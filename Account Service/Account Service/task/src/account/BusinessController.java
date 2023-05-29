package account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BusinessController {

    @GetMapping("/api/empl/payment")
    public void getPayroll() {

    }

    @PostMapping("/api/acct/payments")
    public void uploadPayroll() {

    }

    @PutMapping("/api/acct/payments")
    public void updatePayment() {

    }
}
