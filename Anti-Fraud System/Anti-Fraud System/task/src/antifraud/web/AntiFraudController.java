package antifraud.web;

import antifraud.DTO.ResultDTO;
import antifraud.DTO.TransactionDTO;
import antifraud.model.BankCard;
import antifraud.model.SuspiciousIp;
import antifraud.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/antifraud")
public class AntiFraudController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = {"/transaction", "/transaction/"}, method = RequestMethod.POST)
    // made 2 endpoints with and w/o trailing slash at the end
    @PreAuthorize("hasAuthority('ROLE_MERCHANT')")
    public ResponseEntity makeTransaction(@RequestBody TransactionDTO transactionDTO) {
        try {
            ResultDTO result = transactionService.makeTransaction(transactionDTO);
            return new ResponseEntity(result, HttpStatus.OK);
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

    @PostMapping(value = {"/stolencard", "/stolencard/"})
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

    @DeleteMapping("/stolencard/{number}")
    @PreAuthorize("hasAuthority('ROLE_SUPPORT')")
    public ResponseEntity deleteStolenCardInfo(@PathVariable Long number) {
        try {
            BankCard deletedCard = transactionService.deleteBankCardInfo(number);
            return ResponseEntity.ok(Map.of("status", "Card %d successfully removed!"
                    .formatted(deletedCard.getNumber())));
        } catch (ResponseStatusException exception) {
            return new ResponseEntity(exception.getStatusCode());
        }
    }

    @GetMapping(value = {"/stolencard", "/stolencard/"})
    @PreAuthorize("hasAuthority('ROLE_SUPPORT')")
    public ResponseEntity listCardInfo() {
        var cards = transactionService.getListOfBankCards();
        return ResponseEntity.ok(cards);
    }
}