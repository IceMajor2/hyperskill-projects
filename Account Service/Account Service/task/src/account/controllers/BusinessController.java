package account.controllers;

import account.DTO.PaymentDTO;
import account.models.User;
import account.services.BusinessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity uploadPayroll(@RequestBody List<@Valid PaymentDTO> paycheckDTOS) {
        businessService.uploadPayroll(paycheckDTOS);
        return null;
    }

    @PutMapping("/api/acct/payments")
    @PreAuthorize("hasAuthority('ROLE_ACCOUNTANT')")
    public void updatePayment() {

    }
}
