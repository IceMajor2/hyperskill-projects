package account.controller;

import account.dto.AuthPaymentDTO;
import account.dto.PaymentDTO;
import account.service.BusinessService;
import jakarta.validation.Valid;
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
    public AuthPaymentDTO getPayroll(@AuthenticationPrincipal UserDetails userDetails, @RequestParam String period) {
        return businessService.getPayrolls(userDetails, period);
    }

    @GetMapping(value = {"/api/empl/payment", "/api/empl/payment/"})
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ACCOUNTANT')")
    public List<AuthPaymentDTO> getPayroll(@AuthenticationPrincipal UserDetails userDetails) {
        return businessService.getPayrolls(userDetails);
    }

    @PostMapping(value = {"/api/acct/payments", "/api/acct/payments/"})
    @PreAuthorize("hasAuthority('ROLE_ACCOUNTANT')")
    public Map<String, String> uploadPayroll(@RequestBody List<@Valid PaymentDTO> paymentDTOS) {
        businessService.uploadPayrolls(paymentDTOS);
        return Map.of("status", "Added successfully!");
    }

    @PutMapping(value = {"/api/acct/payments", "/api/acct/payments/"})
    @PreAuthorize("hasAuthority('ROLE_ACCOUNTANT')")
    public Map<String, String> updatePayment(@RequestBody @Valid PaymentDTO paymentDTO) {
        businessService.updatePayment(paymentDTO);
        return Map.of("status", "Updated successfully!");
    }
}
