package antifraud.web;

import antifraud.DTO.ResultDTO;
import antifraud.model.BankCard;
import antifraud.model.SuspiciousIp;
import antifraud.model.Transaction;
import antifraud.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = {"/transaction", "/transaction/"}, method = RequestMethod.POST)
    // made 2 endpoints with and w/o trailing slash at the end
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    public ResponseEntity makeTransaction(@RequestBody Transaction transaction) {
        try {
            ResultDTO result = transactionService.makeTransaction(transaction);
            return new ResponseEntity(Map.of("result", result.getResult()), HttpStatus.OK);
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }

    @PostMapping("/suspicious-ip")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT')")
    public ResponseEntity saveSuspiciousIp(@Valid @RequestBody SuspiciousIp ip) {
        try {
            SuspiciousIp savedIp = transactionService.saveSuspiciousIp(ip);
            return new ResponseEntity(Map.of("id", savedIp.getId(),
                    "ip", savedIp.getIp()), HttpStatus.CREATED);
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT')")
    public ResponseEntity deleteSuspiciousIp(@PathVariable String ip) {
        try {
            SuspiciousIp deletedIp = transactionService.deleteSuspiciousIp(ip);
            return ResponseEntity.ok(Map.of("status", "IP %s successfully removed!"
                    .formatted(deletedIp.getIp())));
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }

    @GetMapping("/suspicious-ip")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT')")
    public ResponseEntity listOfSuspiciousIps() {
        var suspiciousIps = transactionService.listOfSuspiciousIps();
        return ResponseEntity.ok(suspiciousIps);
    }

    @PostMapping("/stolencard")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT')")
    public ResponseEntity saveStolenCardInfo(@RequestBody BankCard stolenCard) {
        try {
            BankCard savedCard = transactionService.saveBankCardInfo(stolenCard);
            return new ResponseEntity(Map.of("id", savedCard.getId(), "number", savedCard.getNumber()),
                    HttpStatus.CREATED);
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }
}