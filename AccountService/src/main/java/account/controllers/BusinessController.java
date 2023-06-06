package account.controllers;

import account.dto.AuthPaymentDTO;
import account.dto.PaymentDTO;
import account.services.BusinessService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public class BusinessController {

    private BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping(value = {"/api/empl/payment", "/api/empl/payment/"}, params = "period")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity getPayroll(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String period) {
        AuthPaymentDTO authPaymentDTO = businessService.getPayrolls(userDetails, period);
        return ResponseEntity.ok(authPaymentDTO);
    }

    @GetMapping(value = {"/api/empl/payment", "/api/empl/payment/"})
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity getPayroll(@AuthenticationPrincipal UserDetails userDetails) {
        List<AuthPaymentDTO> payments = businessService.getPayrolls(userDetails);
        return ResponseEntity.ok(payments);
    }

    @PostMapping(value = {"/api/acct/payments", "/api/acct/payments/"})
    @PreAuthorize("hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity uploadPayroll(@RequestBody List<@Valid PaymentDTO> paymentDTOS) {
        businessService.uploadPayrolls(paymentDTOS);
        return ResponseEntity.ok(Map.of("status", "Added successfully!"));
    }

    @PutMapping(value = {"/api/acct/payments", "/api/acct/payments/"})
    @PreAuthorize("hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity updatePayment(@RequestBody @Valid PaymentDTO paymentDTO) {
        businessService.updatePayment(paymentDTO);
        return ResponseEntity.ok(Map.of("status", "Updated successfully!"));
    }
}
